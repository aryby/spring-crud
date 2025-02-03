package io.aryby.spring_boot_crud.developer_preferences;

import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.NotFoundException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DeveloperPreferencesService {

    private final DeveloperPreferencesRepository developerPreferencesRepository;
    private final ProjectSettingsRepository projectSettingsRepository;

    public DeveloperPreferencesService(
            final DeveloperPreferencesRepository developerPreferencesRepository,
            final ProjectSettingsRepository projectSettingsRepository) {
        this.developerPreferencesRepository = developerPreferencesRepository;
        this.projectSettingsRepository = projectSettingsRepository;
    }

    public List<DeveloperPreferencesDTO> findAll() {
        final List<DeveloperPreferences> developerPreferenceses = developerPreferencesRepository.findAll(Sort.by("id"));
        return developerPreferenceses.stream()
                .map(developerPreferences -> mapToDTO(developerPreferences, new DeveloperPreferencesDTO()))
                .toList();
    }

    public DeveloperPreferencesDTO get(final Long id) {
        return developerPreferencesRepository.findById(id)
                .map(developerPreferences -> mapToDTO(developerPreferences, new DeveloperPreferencesDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final DeveloperPreferencesDTO developerPreferencesDTO) {
        final DeveloperPreferences developerPreferences = new DeveloperPreferences();
        mapToEntity(developerPreferencesDTO, developerPreferences);
        return developerPreferencesRepository.save(developerPreferences).getId();
    }

    public void update(final Long id, final DeveloperPreferencesDTO developerPreferencesDTO) {
        final DeveloperPreferences developerPreferences = developerPreferencesRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(developerPreferencesDTO, developerPreferences);
        developerPreferencesRepository.save(developerPreferences);
    }

    public void delete(final Long id) {
        developerPreferencesRepository.deleteById(id);
    }

    private DeveloperPreferencesDTO mapToDTO(final DeveloperPreferences developerPreferences,
            final DeveloperPreferencesDTO developerPreferencesDTO) {
        developerPreferencesDTO.setId(developerPreferences.getId());
        developerPreferencesDTO.setAppFormat(developerPreferences.getAppFormat());
        developerPreferencesDTO.setPackageStrategy(developerPreferences.getPackageStrategy());
        developerPreferencesDTO.setEnableOpenAPI(developerPreferences.getEnableOpenAPI());
        developerPreferencesDTO.setUseDockerCompose(developerPreferences.getUseDockerCompose());
        developerPreferencesDTO.setJavaVersion(developerPreferences.getJavaVersion());
        developerPreferencesDTO.setFurtherDependencies(developerPreferences.getFurtherDependencies());
        return developerPreferencesDTO;
    }

    private DeveloperPreferences mapToEntity(final DeveloperPreferencesDTO developerPreferencesDTO,
            final DeveloperPreferences developerPreferences) {
        developerPreferences.setAppFormat(developerPreferencesDTO.getAppFormat());
        developerPreferences.setPackageStrategy(developerPreferencesDTO.getPackageStrategy());
        developerPreferences.setEnableOpenAPI(developerPreferencesDTO.getEnableOpenAPI());
        developerPreferences.setUseDockerCompose(developerPreferencesDTO.getUseDockerCompose());
        developerPreferences.setJavaVersion(developerPreferencesDTO.getJavaVersion());
        developerPreferences.setFurtherDependencies(developerPreferencesDTO.getFurtherDependencies());
        return developerPreferences;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final DeveloperPreferences developerPreferences = developerPreferencesRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ProjectSettings developerPreferencesProjectSettings = projectSettingsRepository.findFirstByDeveloperPreferences(developerPreferences.getId());
        if (developerPreferencesProjectSettings != null) {
            referencedWarning.setKey("developerPreferences.projectSettings.developerPreferences.referenced");
            referencedWarning.addParam(developerPreferencesProjectSettings.getId());
            return referencedWarning;
        }
        return null;
    }

}
