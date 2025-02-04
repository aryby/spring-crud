package io.aryby.spring_boot_crud.project_settings;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface ProjectSettingsRepository extends JpaRepository<ProjectSettings, Long> {

        ProjectSettings findFirstByGeneralSettings(Long generalSettings);
        ProjectSettings findFirstByDeveloperPreferences(Long developerPreferences);
        ProjectSettings findFirstByDatabaseSettings(Long databaseSettings);
        Optional<ProjectSettings> findFirstBySlug(String slug);

}
