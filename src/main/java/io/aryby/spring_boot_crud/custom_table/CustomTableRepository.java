package io.aryby.spring_boot_crud.custom_table;

import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributes;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CustomTableRepository extends JpaRepository<CustomTable, Long> {

    CustomTable findFirstByprojectSetting(ProjectSettings projectSetting);

    List<CustomTable> findAllByprojectSetting(Long projectSetting);
}
