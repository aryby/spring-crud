package io.aryby.spring_boot_crud.generator.frontend;


import io.aryby.spring_boot_crud.custom_table.CustomTable;

public interface IModelGenerator {
    String generateAngularModel(CustomTable table, Long projectId);
}
