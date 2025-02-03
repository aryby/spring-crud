package io.aryby.spring_boot_crud.database_settings;

import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.NotFoundException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DatabaseSettingsService {

    private final DatabaseSettingsRepository databaseSettingsRepository;
    private final ProjectSettingsRepository projectSettingsRepository;

    public DatabaseSettingsService(final DatabaseSettingsRepository databaseSettingsRepository,
            final ProjectSettingsRepository projectSettingsRepository) {
        this.databaseSettingsRepository = databaseSettingsRepository;
        this.projectSettingsRepository = projectSettingsRepository;
    }

    public List<DatabaseSettingsDTO> findAll() {
        final List<DatabaseSettings> databaseSettingses = databaseSettingsRepository.findAll(Sort.by("id"));
        return databaseSettingses.stream()
                .map(databaseSettings -> mapToDTO(databaseSettings, new DatabaseSettingsDTO()))
                .toList();
    }

    public DatabaseSettingsDTO get(final Long id) {
        return databaseSettingsRepository.findById(id)
                .map(databaseSettings -> mapToDTO(databaseSettings, new DatabaseSettingsDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final DatabaseSettingsDTO databaseSettingsDTO) {
        final DatabaseSettings databaseSettings = new DatabaseSettings();
        mapToEntity(databaseSettingsDTO, databaseSettings);
        return databaseSettingsRepository.save(databaseSettings).getId();
    }

    public void update(final Long id, final DatabaseSettingsDTO databaseSettingsDTO) {
        final DatabaseSettings databaseSettings = databaseSettingsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(databaseSettingsDTO, databaseSettings);
        databaseSettingsRepository.save(databaseSettings);
    }

    public void delete(final Long id) {
        databaseSettingsRepository.deleteById(id);
    }

    private DatabaseSettingsDTO mapToDTO(final DatabaseSettings databaseSettings,
            final DatabaseSettingsDTO databaseSettingsDTO) {
        databaseSettingsDTO.setId(databaseSettings.getId());
        databaseSettingsDTO.setDatabaseProvider(databaseSettings.getDatabaseProvider());
        databaseSettingsDTO.setAddTimestamps(databaseSettings.getAddTimestamps());
        return databaseSettingsDTO;
    }

    private DatabaseSettings mapToEntity(final DatabaseSettingsDTO databaseSettingsDTO,
            final DatabaseSettings databaseSettings) {
        databaseSettings.setDatabaseProvider(databaseSettingsDTO.getDatabaseProvider());
        databaseSettings.setAddTimestamps(databaseSettingsDTO.getAddTimestamps());
        return databaseSettings;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final DatabaseSettings databaseSettings = databaseSettingsRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ProjectSettings databaseSettingsProjectSettings = projectSettingsRepository.findFirstByDatabaseSettings(databaseSettings.getId());
        if (databaseSettingsProjectSettings != null) {
            referencedWarning.setKey("databaseSettings.projectSettings.databaseSettings.referenced");
            referencedWarning.addParam(databaseSettingsProjectSettings.getId());
            return referencedWarning;
        }
        return null;
    }

}
