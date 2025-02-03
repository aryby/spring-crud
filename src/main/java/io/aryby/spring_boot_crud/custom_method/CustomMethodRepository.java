package io.aryby.spring_boot_crud.custom_method;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomMethodRepository extends JpaRepository<CustomMethod, Long> {

    CustomMethod findFirstByCustomTable(CustomTable customTable);

}
