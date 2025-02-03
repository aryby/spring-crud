package io.aryby.spring_boot_crud.custom_table_attributes;

import io.aryby.spring_boot_crud.util.ReferencedException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/customTableAttributess", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomTableAttributesResource {

    private final CustomTableAttributesService customTableAttributesService;

    public CustomTableAttributesResource(
            final CustomTableAttributesService customTableAttributesService) {
        this.customTableAttributesService = customTableAttributesService;
    }

    @GetMapping
    public ResponseEntity<List<CustomTableAttributesDTO>> getAllCustomTableAttributess() {
        return ResponseEntity.ok(customTableAttributesService.findAll());
    }

    @GetMapping("/customTable/{id}")
    public ResponseEntity<List<CustomTableAttributesDTO>> getAllCustomTableAttributesByTableId(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customTableAttributesService.findAllByTableId(id));
    }



    @GetMapping("/{id}")
    public ResponseEntity<CustomTableAttributesDTO> getCustomTableAttributes(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customTableAttributesService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCustomTableAttributes(
            @RequestBody @Valid final CustomTableAttributesDTO customTableAttributesDTO) {
        System.out.println(customTableAttributesDTO.toString());
        final Long createdId = customTableAttributesService.create(customTableAttributesDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCustomTableAttributes(
            @PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CustomTableAttributesDTO customTableAttributesDTO) {
        customTableAttributesService.update(id, customTableAttributesDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCustomTableAttributes(
            @PathVariable(name = "id") final Long id) {

        customTableAttributesService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
