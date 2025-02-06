package io.aryby.spring_boot_crud.custom_table;

import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttribute;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeRepository;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.CustomCollectors;
import io.aryby.spring_boot_crud.util.ReferencedException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ProjectSettingsRepository projectSettingRepository;
    private final Logger logger = LoggerFactory.getLogger(CustomTableResource.class);

    public CustomTableResource(final CustomTableService customTableService,
            final ProjectSettingsRepository projectSettingRepository) {
        this.customTableService = customTableService;
        this.projectSettingRepository = projectSettingRepository;
    }

    @GetMapping
    public ResponseEntity<List<CustomTableDTO>> getAllCustomTables() {
        logger.info("get All Custom Tables");
        return ResponseEntity.ok(customTableService.findAll());
    }
    @GetMapping("/attributes/{id}")
    public ResponseEntity<List<CustomTableAttribute>> getAllAttributesByTableId(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customTableService.findAllAttributesByTableId(id));
    }

    @GetMapping("/customTable/{id}")
    public ResponseEntity<List<CustomTableAttribute>> getAllAttributesByTable(@PathVariable(name = "id") final Long id) {
        logger.info("Get All Custom Table Attributes by Table id {} ",id);
        return ResponseEntity.ok(customTableService.findAllAttributesByTableId(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomTableDTO> getCustomTable(@PathVariable(name = "id") final Long id) {
        logger.info("get Custom Table by id: {}", id );
        return ResponseEntity.ok(customTableService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCustomTable(
            @RequestBody @Valid final CustomTableDTO customTableDTO) {
        logger.info("Create Custom Table {}", customTableDTO );
        final Long createdId = customTableService.create(customTableDTO);
        logger.info("Created Custom Table {}", createdId);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCustomTable(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CustomTableDTO customTableDTO) {
        logger.info("Update Create Custom Table {}", customTableDTO );
        customTableService.update(id, customTableDTO);
        logger.info("Updated Create Custom Table {}", customTableDTO );

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


    @GetMapping("/projectSettingValues")
    public ResponseEntity<Map<Long, Long>> getprojectSettingValues() {
        return ResponseEntity.ok(projectSettingRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(ProjectSettings::getId, ProjectSettings::getId)));
    }

}
