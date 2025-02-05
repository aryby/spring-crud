package io.aryby.spring_boot_crud.custom_table;

import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttribute;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@ToString
public class CustomTableDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;
    private List<CustomTableAttribute> customTablesAttributes;
    private Long projectSetting;

}
