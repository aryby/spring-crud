package io.aryby.spring_boot_crud.project_settings;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProjectSettingsDTO {

    private Long id;

    private Long generalSettings;

    private Long databaseSettings;

    private Long developerPreferences;

}
