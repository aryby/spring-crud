package io.aryby.spring_boot_crud.general_settings;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GeneralSettingsDTO {

    private Long id;

    @Size(max = 255)
    private String projectName;

    private String groupId;

    private String artifactId;

    @Size(max = 255)
    private String buildType;

    @Size(max = 255)
    private String language;

    private Boolean enableLombok;

    @Size(max = 255)
    private String frontendType;

}
