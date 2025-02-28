package io.aryby.spring_boot_crud.generator.frontend;

import io.aryby.spring_boot_crud.custom_table.CustomTable;

public interface IMvcComponentList {
    String generate(CustomTable table, Long projectId);
}
