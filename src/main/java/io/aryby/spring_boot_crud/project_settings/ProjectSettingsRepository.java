package io.aryby.spring_boot_crud.project_settings;

import io.aryby.spring_boot_crud.database_settings.DatabaseSettings;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProjectSettingsRepository extends JpaRepository<ProjectSettings, Long> {
//
//    ProjectSettings findFirstByGeneralSettings(GeneralSettings generalSettings);
//    ProjectSettings findFirstByDeveloperPreferences(DeveloperPreferences developerPreferences);
//    ProjectSettings findFirstByDatabaseSettings(DatabaseSettings databaseSettings);

    ProjectSettings findFirstByGeneralSettings(Long generalSettings);
    ProjectSettings findFirstByDeveloperPreferences(Long developerPreferences);
    ProjectSettings findFirstByDatabaseSettings(Long databaseSettings);
    boolean existsByGeneralSettings(Long id);

    boolean existsByDatabaseSettings(Long id);

    boolean existsByDeveloperPreferences(Long id);

}
