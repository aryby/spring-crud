package io.aryby.spring_boot_crud.generator;


import io.aryby.spring_boot_crud.custom_table.CustomTable;

public interface IServiceGenerator {
    String generate(CustomTable table, Long projectId);
}
