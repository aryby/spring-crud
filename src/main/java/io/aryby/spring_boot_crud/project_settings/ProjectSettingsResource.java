package io.aryby.spring_boot_crud.project_settings;

import io.aryby.spring_boot_crud.database_settings.DatabaseSettings;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettingsRepository;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferencesRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IGenerateZip;
import io.aryby.spring_boot_crud.generator.implimentations.IGenerateBackendZipImpl;
import io.aryby.spring_boot_crud.generator.implimentations.IGenerateFrontendZipImpl;
import io.aryby.spring_boot_crud.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/projectSettings", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProjectSettingsResource {

    private final Logger logger = LoggerFactory.getLogger(ProjectSettingsResource.class);
    private final ProjectSettingService projectSettingService;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final DatabaseSettingsRepository databaseSettingsRepository;
    private final DeveloperPreferencesRepository developerPreferencesRepository;
    private final IGenerateZip generateBackendZip;
    private final IGenerateZip generateFrontendZip;

    public ProjectSettingsResource(final ProjectSettingService projectSettingService,
                                   final GeneralSettingsRepository generalSettingsRepository,
                                   final DatabaseSettingsRepository databaseSettingsRepository,
                                   final DeveloperPreferencesRepository developerPreferencesRepository,
                                   @Qualifier("IGenerateBackendZipImpl") IGenerateZip generateBackendZip,
                                   @Qualifier("IGenerateFrontendZipImpl") IGenerateZip generateFrontendZip) {
        this.projectSettingService = projectSettingService;
        this.generalSettingsRepository = generalSettingsRepository;
        this.databaseSettingsRepository = databaseSettingsRepository;
        this.developerPreferencesRepository = developerPreferencesRepository;
        this.generateBackendZip = generateBackendZip;
        this.generateFrontendZip = generateFrontendZip;
    }

    @GetMapping
    public ResponseEntity<List<ProjectSettingsDTO>> getAllProjectSettings() {
        return ResponseEntity.ok(projectSettingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectSettingsDTO> getProjectSetting(
            @PathVariable(name = "id") final Long id) {
        logger.info("Get project setting by id: {}", id);
        return ResponseEntity.ok(projectSettingService.get(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProjectSettingsDTO> getProjectSettingBySlug(
        @PathVariable(name = "slug") final String slug) {
        logger.info("Get project setting by slug: {}", slug);
        return ResponseEntity.ok(projectSettingService.getBySlug(slug));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<ProjectSettings> createProjectSetting(
            @RequestBody @Valid final ProjectSettingsDTO projectSettingDTO) {
        logger.info("Create project setting: {}", projectSettingDTO);
        final ProjectSettings created = projectSettingService.create(projectSettingDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProjectSetting(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProjectSettingsDTO projectSettingDTO) {
        logger.info("Update project setting: {}", projectSettingDTO);
        projectSettingService.update(id, projectSettingDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProjectSetting(@PathVariable(name = "id") final Long id) {
        logger.info("Delete project setting: {}", id);
        projectSettingService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/download/{id}")
    public byte[] generateBackZip(@PathVariable(name = "id") final Long id) throws IOException {
        logger.info("Generate zip file: {}", id);
        //return generateFrontendZip.generateZip(id);
        return generateBackendZip.generateZip(id);
    }

    @GetMapping("/download/front/{id}")
    public byte[] generateFontZip(@PathVariable(name = "id") final Long id) throws IOException {
        logger.info("Generate zip file: {}", id);
        return generateFrontendZip.generateZip(id);
        //return generateBackendZip.generateZip(id);
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
