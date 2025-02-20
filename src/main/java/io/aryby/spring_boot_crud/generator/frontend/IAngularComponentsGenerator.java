package io.aryby.spring_boot_crud.generator.frontend;


import io.aryby.spring_boot_crud.custom_table.CustomTable;

public interface IAngularComponentsGenerator {
    String generateAngularComponentsTs(CustomTable table, Long projectId);
    String generateAngularComponentsHtml(CustomTable table, Long projectId);
}
