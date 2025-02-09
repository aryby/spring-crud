package io.aryby.spring_boot_crud.generator.implimentations;


import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IRequestGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service  // Register as a Spring Bean
public class RequestImpl implements IRequestGenerator {

    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    // ✅ Constructor Injection
    public RequestImpl(ProjectSettingsRepository projectSettingsRepository,
                       GeneralSettingsRepository generalSettingsRepository,
                       CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generateRequestClass(CustomTable table, Long projectId) {
        System.out.println("Generating Request Class for: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(generalSettings.getGroupId()).append(".")
            .append(generalSettings.getArtifactId()).append(".requests;\n\n");

        sb.append("""
                import jakarta.validation.constraints.NotBlank;
                import lombok.*;

                @Getter
                @Setter
                @NoArgsConstructor
                @AllArgsConstructor
                """);

        sb.append("public class ").append(table.getName()).append("Request {\n");


        List<CustomTableAttributeDTO> attributes = customTableAttributeService.findAllByTableId(table.getId());

        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("     @NotBlank(message = \""+attr.getNameAttribute()+" is required\")    " +
                    "\n     private ")
                            .append(attr.getNameTypeModifier())
                            .append(" ")
                            .append(attr.getNameAttribute()).append(";\n");
        }



        sb.append("}");

        return sb.toString();
    }
}
