package io.aryby.spring_boot_crud.custom_method;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/customMethods", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomMethodResource {

    private final CustomMethodService customMethodService;
    private final CustomTableRepository customTableRepository;

    public CustomMethodResource(final CustomMethodService customMethodService,
            final CustomTableRepository customTableRepository) {
        this.customMethodService = customMethodService;
        this.customTableRepository = customTableRepository;
    }

    @GetMapping
    public ResponseEntity<List<CustomMethodDTO>> getAllCustomMethods() {
        return ResponseEntity.ok(customMethodService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomMethodDTO> getCustomMethod(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customMethodService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCustomMethod(
            @RequestBody @Valid final CustomMethodDTO customMethodDTO) {
        final Long createdId = customMethodService.create(customMethodDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCustomMethod(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CustomMethodDTO customMethodDTO) {
        customMethodService.update(id, customMethodDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCustomMethod(@PathVariable(name = "id") final Long id) {
        customMethodService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customTableValues")
    public ResponseEntity<Map<Long, String>> getCustomTableValues() {
        return ResponseEntity.ok(customTableRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(CustomTable::getId, CustomTable::getName)));
    }

}
