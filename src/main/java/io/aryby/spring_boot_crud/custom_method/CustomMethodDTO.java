package io.aryby.spring_boot_crud.custom_method;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomMethodDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String methodName;

    private String methodBody;

    private String annotations;

    private Long customTable;

}
