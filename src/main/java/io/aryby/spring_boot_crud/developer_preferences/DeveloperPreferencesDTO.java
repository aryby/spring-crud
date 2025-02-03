package io.aryby.spring_boot_crud.developer_preferences;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DeveloperPreferencesDTO {

    private Long id;

    @Size(max = 255)
    private String appFormat;

    @Size(max = 255)
    private String packageStrategy;

    private Boolean enableOpenAPI;

    private Boolean useDockerCompose;

    @Size(max = 255)
    private String javaVersion;

    private String furtherDependencies;

}
