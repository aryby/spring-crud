package io.aryby.spring_boot_crud.generator.jpa_generator;

import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;

import java.util.List;

public interface IJpaGetAll {

    public StringBuilder getAll(String DTO_MODAL, String ENTITY_MODAL, String REPO_DI_LOWER, List<CustomTableAttributeDTO> attributes ) ;
}
