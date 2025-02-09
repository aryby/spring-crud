package io.aryby.spring_boot_crud.generator.implimentations;


import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IDTOGenerator;
import io.aryby.spring_boot_crud.generator.IEntityGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.CapitalizeFirstChar;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // Register as a Spring Bean
public class DTOGeneratorImpl implements IDTOGenerator {

    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    // ✅ Constructor Injection
    public DTOGeneratorImpl(ProjectSettingsRepository projectSettingsRepository,
                            GeneralSettingsRepository generalSettingsRepository,
                            CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generateDTOClass(CustomTable table, Long projectId) {
        System.out.println("Generating Java Class for: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(generalSettings.getGroupId()).append(".")
            .append(generalSettings.getArtifactId()).append(".dtos;\n\n");

        sb.append("""
                    import jakarta.persistence.*;
                    import lombok.*;
                    import java.time.OffsetDateTime;


                    @Getter
                    @Setter
                    @NoArgsConstructor
                    @AllArgsConstructor
                    @Builder
                    """);

        sb.append("public class ").append(table.getName()).append("DTO {\n");

        sb.append("""
                private Long id;
            """);

        List<CustomTableAttributeDTO> attributes = customTableAttributeService.findAllByTableId(table.getId());

        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("    private ").append(attr.getNameTypeModifier()).append(" ")
                .append(attr.getNameAttribute()).append(";\n");
        }

        sb.append("""
                private OffsetDateTime lastUpdated;
            """);

        sb.append("}");

        return sb.toString();
    }
}
