package io.aryby.spring_boot_crud.project_settings;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.database_settings.DatabaseSettings;
import io.aryby.spring_boot_crud.developer_preferences.DeveloperPreferences;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "ProjectSettingses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ProjectSettings {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JoinColumn(name = "general_settings_id", unique = true)
    private Long generalSettings;


    @JoinColumn(name = "database_settings_id", unique = true)
    private Long databaseSettings;


    @JoinColumn(name = "developer_preferences_id", unique = true)
    private Long developerPreferences;


    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
