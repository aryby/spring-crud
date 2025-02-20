package io.aryby.spring_boot_crud.generator.implimentations;


import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IEntityGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // Register as a Spring Bean
public class EntityGeneratorImpl implements IEntityGenerator {

    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;


    // ✅ Constructor Injection
    public EntityGeneratorImpl(ProjectSettingsRepository projectSettingsRepository,
                               GeneralSettingsRepository generalSettingsRepository,
                               CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generateJavaClass(CustomTable table, Long projectId) {
        System.out.println("Generating Java Class for: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(generalSettings.getGroupId()).append(".")
            .append(generalSettings.getArtifactId()).append(".entities;\n\n");

        sb.append("""
                import jakarta.persistence.*;
                import java.time.OffsetDateTime;
                import lombok.*;
                import org.springframework.data.annotation.CreatedDate;
                import org.springframework.data.annotation.LastModifiedDate;
                import org.springframework.data.jpa.domain.support.AuditingEntityListener;

                @Entity
                @Table(name = "%s")
                @EntityListeners(AuditingEntityListener.class)
                @Getter
                @Setter
                @NoArgsConstructor
                @AllArgsConstructor
                @Builder
                """.formatted(MyHelpper.lowerCaseFirstLetter(table.getName().toLowerCase()) + "s"));

        sb.append("public class ").append(table.getName()).append(" {\n");

        sb.append("""
                    @Id
                    @Column(nullable = false, updatable = false)
                    @GeneratedValue(strategy = GenerationType.IDENTITY)
                    private Long id;
                """);

        List<CustomTableAttributeDTO> attributes = customTableAttributeService.findAllByTableId(table.getId());

        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("    private ").append(attr.getNameTypeModifier()).append(" ")
                .append(attr.getNameAttribute()).append(";\n");
        }

        sb.append("""
                    @CreatedDate
                    @Column(nullable = false, updatable = false)
                    private OffsetDateTime dateCreated;

                    @LastModifiedDate
                    @Column(nullable = false)
                    private OffsetDateTime lastUpdated;
                """);

        sb.append("}");

        return sb.toString();
    }
}
