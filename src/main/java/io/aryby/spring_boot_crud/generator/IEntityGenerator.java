package io.aryby.spring_boot_crud.generator;


import io.aryby.spring_boot_crud.custom_table.CustomTable;

public interface IEntityGenerator {
    String generateJavaClass(CustomTable table, Long projectId);
}
