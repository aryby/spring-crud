package io.aryby.spring_boot_crud.custom_table_attributes;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CustomTableAttributesRepository extends JpaRepository<CustomTableAttributes, Long> {
    List<CustomTableAttributes> findByCustomTable(Long customTable);
}
