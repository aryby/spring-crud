package io.aryby.spring_boot_crud.custom_table;

import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributes;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributesRepository;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.CustomCollectors;
import io.aryby.spring_boot_crud.util.ReferencedException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
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
@RequestMapping(value = "/api/customTables", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomTableResource {

    private final CustomTableService customTableService;
    private final CustomTableAttributesRepository customTableAttributesRepository;
    private final ProjectSettingsRepository projectSettingRepository;

    public CustomTableResource(final CustomTableService customTableService,
            final CustomTableAttributesRepository customTableAttributesRepository,
            final ProjectSettingsRepository projectSettingRepository) {
        this.customTableService = customTableService;
        this.customTableAttributesRepository = customTableAttributesRepository;
        this.projectSettingRepository = projectSettingRepository;
    }

    @GetMapping
    public ResponseEntity<List<CustomTableDTO>> getAllCustomTables() {
        return ResponseEntity.ok(customTableService.findAll());
    }
    @GetMapping("/attributes/{id}")
    public ResponseEntity<List<CustomTableAttributes>> getAllAttributesByTableId(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customTableAttributesRepository.findByCustomTable(id));
    }

    @GetMapping("/customTable/{id}")
    public ResponseEntity<List<CustomTableAttributes>> getAllAttributesByTable(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customTableAttributesRepository.findByCustomTable(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomTableDTO> getCustomTable(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customTableService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCustomTable(
            @RequestBody @Valid final CustomTableDTO customTableDTO) {
        final Long createdId = customTableService.create(customTableDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCustomTable(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CustomTableDTO customTableDTO) {
        customTableService.update(id, customTableDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCustomTable(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = customTableService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        customTableService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/customTableAttributesValues")
    public ResponseEntity<Map<Long, String>> getCustomTableAttributesValues() {
        return ResponseEntity.ok(customTableAttributesRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(CustomTableAttributes::getId, CustomTableAttributes::getNameAttribute)));
    }

    @GetMapping("/projectSettingValues")
    public ResponseEntity<Map<Long, Long>> getprojectSettingValues() {
        return ResponseEntity.ok(projectSettingRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ProjectSettings::getId, ProjectSettings::getId)));
    }

}
