package io.aryby.spring_boot_crud.custom_table_attributes;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class CustomTableAttributeDTO {

    private Long id;

    @Size(max = 255)
    private String nameTypeModifier;

    @NotNull
    @Size(max = 255)
    private String nameAttribute;

    private String sizeJpaAttributes;

    private String customJoins;

    private String customRelations;

    private Long customTable;

}
