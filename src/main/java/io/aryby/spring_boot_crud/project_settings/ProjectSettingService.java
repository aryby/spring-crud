package io.aryby.spring_boot_crud.project_settings;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettings;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettingsRepository;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferencesRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.*;
import io.aryby.spring_boot_crud.generator.date_defaults_generator.IDateTimeGenerator;
import io.aryby.spring_boot_crud.generator.files_generator.IUploadImage;
import io.aryby.spring_boot_crud.util.NotFoundException;


import java.util.List;

import io.aryby.spring_boot_crud.util.UuidGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProjectSettingService {

    private Logger logger = LoggerFactory.getLogger(ProjectSettingService.class);
    private final ProjectSettingsRepository projectSettingRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final DatabaseSettingsRepository databaseSettingsRepository;
    private final DeveloperPreferencesRepository developerPreferencesRepository;

    public ProjectSettingService(final ProjectSettingsRepository projectSettingRepository,
                                 final GeneralSettingsRepository generalSettingsRepository,
                                 final DatabaseSettingsRepository databaseSettingsRepository,
                                 final DeveloperPreferencesRepository developerPreferencesRepository,
                                 final CustomTableRepository customTableRepository,
                                 final CustomTableAttributeService customTableAttributeService,
                                 final IRepositoryGenerator interfaceGenerator,
                                 final IControllerGenerator controllerGenerator,
                                 final IDTOGenerator dtoGenerator,
                                 final IServiceGenerator serviceGenerator,
                                 final IRequestGenerator requestGenerator,
                                 final IUploadImage uploadImage,
                                 final IEntityGenerator entityGenerator, IMainGenerator mainGenerator, IPomGenerator generatePomXml, IDateTimeGenerator dateTimeGenerator) {
        this.projectSettingRepository = projectSettingRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.databaseSettingsRepository = databaseSettingsRepository;
        this.developerPreferencesRepository = developerPreferencesRepository;
    }

    public List<ProjectSettingsDTO> findAll() {
        final List<ProjectSettings> projectSettinges = projectSettingRepository.findAll(Sort.by("id"));
        return projectSettinges.stream()
            .map(projectSettings -> mapToDTO(projectSettings, new ProjectSettingsDTO()))
            .toList();
    }

    public ProjectSettingsDTO getBySlug(final String slug) {
        logger.info("getBySlug {}", slug);
        return projectSettingRepository.findFirstBySlug(slug)
            .map(projectSetting -> mapToDTO(projectSetting, new ProjectSettingsDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public ProjectSettingsDTO get(final Long id) {
        return projectSettingRepository.findById(id)
            .map(projectSetting -> mapToDTO(projectSetting, new ProjectSettingsDTO()))
            .orElseThrow(NotFoundException::new);
    }

    public ProjectSettings create(final ProjectSettingsDTO projectSettingDTO) {
        final ProjectSettings projectSetting = new ProjectSettings();
        projectSettingDTO.setSlug(UuidGenerator.generateUID());

        mapToEntity(projectSettingDTO, projectSetting);
        return projectSettingRepository.save(projectSetting);
    }

    public void update(final Long id, final ProjectSettingsDTO projectSettingDTO) {
        final ProjectSettings projectSetting = projectSettingRepository.findById(id)
            .orElseThrow(NotFoundException::new);
        mapToEntity(projectSettingDTO, projectSetting);
        projectSettingRepository.save(projectSetting);
    }

    public void delete(final Long id) {
        projectSettingRepository.deleteById(id);
    }

    private ProjectSettingsDTO mapToDTO(final ProjectSettings projectSetting,
                                        final ProjectSettingsDTO projectSettingDTO) {
        projectSettingDTO.setId(projectSetting.getId());
        projectSettingDTO.setSlug(projectSetting.getSlug());
        projectSettingDTO.setGeneralSettings(projectSetting.getGeneralSettings() == null ? null : projectSetting.getGeneralSettings());
        projectSettingDTO.setDatabaseSettings(projectSetting.getDatabaseSettings() == null ? null : projectSetting.getDatabaseSettings());
        projectSettingDTO.setDeveloperPreferences(projectSetting.getDeveloperPreferences() == null ? null : projectSetting.getDeveloperPreferences());
        return projectSettingDTO;
    }

    private ProjectSettings mapToEntity(final ProjectSettingsDTO projectSettingDTO,
                                        final ProjectSettings projectSetting) {
        projectSetting.setId(projectSettingDTO.getId());
        projectSetting.setSlug(projectSettingDTO.getSlug());
        final GeneralSettings generalSettings = projectSettingDTO.getGeneralSettings() == null ? null : generalSettingsRepository.findById(projectSettingDTO.getGeneralSettings())
            .orElseThrow(() -> new NotFoundException("generalSettings not found"));
        projectSetting.setGeneralSettings(generalSettings.getId());
        final DatabaseSettings databaseSettings = projectSettingDTO.getDatabaseSettings() == null ? null : databaseSettingsRepository.findById(projectSettingDTO.getDatabaseSettings())
            .orElseThrow(() -> new NotFoundException("databaseSettings not found"));
        projectSetting.setDatabaseSettings(databaseSettings.getId());
        final DeveloperPreferences developerPreferences = projectSettingDTO.getDeveloperPreferences() == null ? null : developerPreferencesRepository.findById(projectSettingDTO.getDeveloperPreferences())
            .orElseThrow(() -> new NotFoundException("developerPreferences not found"));
        projectSetting.setDeveloperPreferences(developerPreferences.getId());
        return projectSetting;
    }




}
