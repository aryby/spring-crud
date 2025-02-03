package io.aryby.spring_boot_crud.developer_preferences;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "DeveloperPreferenceses")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class DeveloperPreferences {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String appFormat;

    @Column
    private String packageStrategy;

    @Column(columnDefinition = "tinyint", length = 1)
    private Boolean enableOpenAPI;

    @Column(columnDefinition = "tinyint", length = 1)
    private Boolean useDockerCompose;

    @Column
    private String javaVersion;

    @Column(columnDefinition = "longtext")
    private String furtherDependencies;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
