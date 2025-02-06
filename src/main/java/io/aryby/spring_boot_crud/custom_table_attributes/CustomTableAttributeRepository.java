package io.aryby.spring_boot_crud.custom_table_attributes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomTableAttributeRepository extends JpaRepository<CustomTableAttribute, Long> {
    List<CustomTableAttribute> findAllByCustomTable(Long customTable);
}
