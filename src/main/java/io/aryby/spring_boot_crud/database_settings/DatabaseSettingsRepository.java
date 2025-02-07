package io.aryby.spring_boot_crud.database_settings;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseSettingsRepository extends JpaRepository<DatabaseSettings, Long> {
}
