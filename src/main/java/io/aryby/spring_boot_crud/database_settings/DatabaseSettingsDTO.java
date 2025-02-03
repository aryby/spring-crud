package io.aryby.spring_boot_crud.database_settings;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DatabaseSettingsDTO {

    private Long id;

    @Size(max = 255)
    private String databaseProvider;

    private Boolean addTimestamps;

}
