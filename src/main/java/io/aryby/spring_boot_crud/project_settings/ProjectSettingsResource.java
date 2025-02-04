package io.aryby.spring_boot_crud.project_settings;

import io.aryby.spring_boot_crud.database_settings.DatabaseSettings;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettingsRepository;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferencesRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/projectSettings", produces = MediaType.APPLICATION_JSON_VALUE)
public class projectSettingResource {

    private final projectSettingService projectSettingService;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final DatabaseSettingsRepository databaseSettingsRepository;
    private final DeveloperPreferencesRepository developerPreferencesRepository;

    public projectSettingResource(final projectSettingService projectSettingService,
            final GeneralSettingsRepository generalSettingsRepository,
            final DatabaseSettingsRepository databaseSettingsRepository,
            final DeveloperPreferencesRepository developerPreferencesRepository) {
        this.projectSettingService = projectSettingService;
        this.generalSettingsRepository = generalSettingsRepository;
        this.databaseSettingsRepository = databaseSettingsRepository;
        this.developerPreferencesRepository = developerPreferencesRepository;
    }

    @GetMapping
    public ResponseEntity<List<projectSettingDTO>> getAllprojectSettings() {
        return ResponseEntity.ok(projectSettingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<projectSettingDTO> getprojectSetting(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(projectSettingService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createprojectSetting(
            @RequestBody @Valid final projectSettingDTO projectSettingDTO) {
        final Long createdId = projectSettingService.create(projectSettingDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateprojectSetting(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final projectSettingDTO projectSettingDTO) {
        projectSettingService.update(id, projectSettingDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteprojectSetting(@PathVariable(name = "id") final Long id) {

        projectSettingService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/download/{id}")
    public byte[] generateZip(@PathVariable(name = "id") final Long id) throws IOException {
        return projectSettingService.generateZip(id);
    }

    @GetMapping("/generalSettingsValues")
    public ResponseEntity<Map<Long, Long>> getGeneralSettingsValues() {
        return ResponseEntity.ok(generalSettingsRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(GeneralSettings::getId, GeneralSettings::getId)));
    }

    @GetMapping("/databaseSettingsValues")
    public ResponseEntity<Map<Long, Long>> getDatabaseSettingsValues() {
        return ResponseEntity.ok(databaseSettingsRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(DatabaseSettings::getId, DatabaseSettings::getId)));
    }

    @GetMapping("/developerPreferencesValues")
    public ResponseEntity<Map<Long, Long>> getDeveloperPreferencesValues() {
        return ResponseEntity.ok(developerPreferencesRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(DeveloperPreferences::getId, DeveloperPreferences::getId)));
    }


}
