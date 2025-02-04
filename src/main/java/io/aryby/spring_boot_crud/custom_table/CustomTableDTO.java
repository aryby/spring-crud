package io.aryby.spring_boot_crud.custom_table;

import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributes;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Getter
@Setter
public class CustomTableDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;
    private List<CustomTableAttributes> customTablesAttributes;
    private Long projectSetting;

}
