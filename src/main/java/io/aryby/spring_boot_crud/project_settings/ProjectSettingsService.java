package io.aryby.spring_boot_crud.project_settings;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettings;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettingsRepository;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferencesRepository;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.util.NotFoundException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProjectSettingsService {

    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final DatabaseSettingsRepository databaseSettingsRepository;
    private final DeveloperPreferencesRepository developerPreferencesRepository;
    private final CustomTableRepository customTableRepository;

    public ProjectSettingsService(final ProjectSettingsRepository projectSettingsRepository,
            final GeneralSettingsRepository generalSettingsRepository,
            final DatabaseSettingsRepository databaseSettingsRepository,
            final DeveloperPreferencesRepository developerPreferencesRepository,
            final CustomTableRepository customTableRepository) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.databaseSettingsRepository = databaseSettingsRepository;
        this.developerPreferencesRepository = developerPreferencesRepository;
        this.customTableRepository = customTableRepository;
    }

    public List<ProjectSettingsDTO> findAll() {
        final List<ProjectSettings> projectSettingses = projectSettingsRepository.findAll(Sort.by("id"));
        return projectSettingses.stream()
                .map(projectSettings -> mapToDTO(projectSettings, new ProjectSettingsDTO()))
                .toList();
    }

    public ProjectSettingsDTO get(final Long id) {
        return projectSettingsRepository.findById(id)
                .map(projectSettings -> mapToDTO(projectSettings, new ProjectSettingsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProjectSettingsDTO projectSettingsDTO) {
        final ProjectSettings projectSettings = new ProjectSettings();
        mapToEntity(projectSettingsDTO, projectSettings);
        return projectSettingsRepository.save(projectSettings).getId();
    }

    public void update(final Long id, final ProjectSettingsDTO projectSettingsDTO) {
        final ProjectSettings projectSettings = projectSettingsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(projectSettingsDTO, projectSettings);
        projectSettingsRepository.save(projectSettings);
    }

    public void delete(final Long id) {
        projectSettingsRepository.deleteById(id);
    }

    private ProjectSettingsDTO mapToDTO(final ProjectSettings projectSettings,
            final ProjectSettingsDTO projectSettingsDTO) {
        projectSettingsDTO.setId(projectSettings.getId());
        projectSettingsDTO.setGeneralSettings(projectSettings.getGeneralSettings() == null ? null : projectSettings.getGeneralSettings());
        projectSettingsDTO.setDatabaseSettings(projectSettings.getDatabaseSettings() == null ? null : projectSettings.getDatabaseSettings());
        projectSettingsDTO.setDeveloperPreferences(projectSettings.getDeveloperPreferences() == null ? null : projectSettings.getDeveloperPreferences());
        return projectSettingsDTO;
    }

    private ProjectSettings mapToEntity(final ProjectSettingsDTO projectSettingsDTO,
            final ProjectSettings projectSettings) {
        final GeneralSettings generalSettings = projectSettingsDTO.getGeneralSettings() == null ? null : generalSettingsRepository.findById(projectSettingsDTO.getGeneralSettings())
                .orElseThrow(() -> new NotFoundException("generalSettings not found"));
        projectSettings.setGeneralSettings(generalSettings.getId());
        final DatabaseSettings databaseSettings = projectSettingsDTO.getDatabaseSettings() == null ? null : databaseSettingsRepository.findById(projectSettingsDTO.getDatabaseSettings())
                .orElseThrow(() -> new NotFoundException("databaseSettings not found"));
        projectSettings.setDatabaseSettings(databaseSettings.getId());
        final DeveloperPreferences developerPreferences = projectSettingsDTO.getDeveloperPreferences() == null ? null : developerPreferencesRepository.findById(projectSettingsDTO.getDeveloperPreferences())
                .orElseThrow(() -> new NotFoundException("developerPreferences not found"));
        projectSettings.setDeveloperPreferences(developerPreferences.getId());
        return projectSettings;
    }

    public boolean generalSettingsExists(final Long id) {
        return projectSettingsRepository.existsByGeneralSettings(id);
    }

    public boolean databaseSettingsExists(final Long id) {
        return projectSettingsRepository.existsByDatabaseSettings(id);
    }

    public boolean developerPreferencesExists(final Long id) {
        return projectSettingsRepository.existsByDeveloperPreferences(id);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final ProjectSettings projectSettings = projectSettingsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final CustomTable projectSettingsCustomTable = customTableRepository.findFirstByProjectSettings(projectSettings);
        if (projectSettingsCustomTable != null) {
            referencedWarning.setKey("projectSettings.customTable.projectSettings.referenced");
            referencedWarning.addParam(projectSettingsCustomTable.getId());
            return referencedWarning;
        }
        return null;
    }

}
