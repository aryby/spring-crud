package io.aryby.spring_boot_crud.project_settings;


import org.springframework.data.jpa.repository.JpaRepository;



public interface ProjectSettingsRepository extends JpaRepository<ProjectSettings, Long> {

        ProjectSettings findFirstByGeneralSettings(Long generalSettings);
        ProjectSettings findFirstByDeveloperPreferences(Long developerPreferences);
        ProjectSettings findFirstByDatabaseSettings(Long databaseSettings);
    boolean existsByGeneralSettings(Long id);

    boolean existsByDatabaseSettings(Long id);

    boolean existsByDeveloperPreferences(Long id);

}
