package io.aryby.spring_boot_crud.general_settings;

import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.NotFoundException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class GeneralSettingsService {

    private final GeneralSettingsRepository generalSettingsRepository;
    private final ProjectSettingsRepository projectSettingsRepository;

    public GeneralSettingsService(final GeneralSettingsRepository generalSettingsRepository,
            final ProjectSettingsRepository projectSettingsRepository) {
        this.generalSettingsRepository = generalSettingsRepository;
        this.projectSettingsRepository = projectSettingsRepository;
    }

    public List<GeneralSettingsDTO> findAll() {
        final List<GeneralSettings> generalSettingses = generalSettingsRepository.findAll(Sort.by("id"));
        return generalSettingses.stream()
                .map(generalSettings -> mapToDTO(generalSettings, new GeneralSettingsDTO()))
                .toList();
    }

    public GeneralSettingsDTO get(final Long id) {
        return generalSettingsRepository.findById(id)
                .map(generalSettings -> mapToDTO(generalSettings, new GeneralSettingsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final GeneralSettingsDTO generalSettingsDTO) {
        final GeneralSettings generalSettings = new GeneralSettings();
        mapToEntity(generalSettingsDTO, generalSettings);
        return generalSettingsRepository.save(generalSettings).getId();
    }

    public void update(final Long id, final GeneralSettingsDTO generalSettingsDTO) {
        final GeneralSettings generalSettings = generalSettingsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(generalSettingsDTO, generalSettings);
        generalSettingsRepository.save(generalSettings);
    }

    public void delete(final Long id) {
        generalSettingsRepository.deleteById(id);
    }

    private GeneralSettingsDTO mapToDTO(final GeneralSettings generalSettings,
            final GeneralSettingsDTO generalSettingsDTO) {
        generalSettingsDTO.setId(generalSettings.getId());
        generalSettingsDTO.setArtifactId(generalSettings.getArtifactId());
        generalSettingsDTO.setGroupId(generalSettings.getGroupId());
        generalSettingsDTO.setProjectName(generalSettings.getProjectName());
        generalSettingsDTO.setBuildType(generalSettings.getBuildType());
        generalSettingsDTO.setLanguage(generalSettings.getLanguage());
        generalSettingsDTO.setEnableLombok(generalSettings.getEnableLombok());
        generalSettingsDTO.setFrontendType(generalSettings.getFrontendType());
        return generalSettingsDTO;
    }

    private GeneralSettings mapToEntity(final GeneralSettingsDTO generalSettingsDTO,
            final GeneralSettings generalSettings) {
        generalSettings.setProjectName(generalSettingsDTO.getProjectName());
        generalSettings.setArtifactId(generalSettingsDTO.getArtifactId());
        generalSettings.setGroupId(generalSettingsDTO.getGroupId());
        generalSettings.setBuildType(generalSettingsDTO.getBuildType());
        generalSettings.setLanguage(generalSettingsDTO.getLanguage());
        generalSettings.setEnableLombok(generalSettingsDTO.getEnableLombok());
        generalSettings.setFrontendType(generalSettingsDTO.getFrontendType());
        return generalSettings;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final GeneralSettings generalSettings = generalSettingsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ProjectSettings generalSettingsProjectSettings = projectSettingsRepository.findFirstByGeneralSettings(generalSettings.getId());
        if (generalSettingsProjectSettings != null) {
            referencedWarning.setKey("generalSettings.projectSettings.generalSettings.referenced");
            referencedWarning.addParam(generalSettingsProjectSettings.getId());
            return referencedWarning;
        }
        return null;
    }

}
