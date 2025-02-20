package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IRepositoryGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryGeneratorImpl implements IRepositoryGenerator {

    private Logger logger = LoggerFactory.getLogger(RepositoryGeneratorImpl.class);
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    // ✅ Constructor Injection
    public RepositoryGeneratorImpl(ProjectSettingsRepository projectSettingsRepository,
                                   GeneralSettingsRepository generalSettingsRepository,
                                   CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generate(CustomTable table, Long projectId) {
        logger.info("Generate Interface in precess ...");
        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));
        logger.info("ProjectSettings found for ID: " + projectId);
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));
        logger.info("GeneralSettings found for ID: " + projectId);

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(generalSettings.getGroupId()).append(".")
            .append(generalSettings.getArtifactId()).append(".repositories;\n\n");

        String serviceFormatedName = MyHelpper.capitalizeFirstLetter(table.getName()) + "Repository";

        sb.append("import ")
            .append(generalSettings.getGroupId())
            .append(".")
            .append(generalSettings.getArtifactId())
            .append(".entities.")
            .append(table.getName())
            .append(";\n");

        sb.append("""
            import java.util.Optional;
            import org.springframework.data.jpa.repository.JpaRepository;
            import org.springframework.stereotype.Repository;

            @Repository
            """);

        sb.append("public interface ").
            append(serviceFormatedName).
            append("  extends JpaRepository<");
        sb.append(MyHelpper.capitalizeFirstLetter(table.getName()));
        sb.append(", Long>  {\n");

        List<CustomTableAttributeDTO> attributes = customTableAttributeService.findAllByTableId(table.getId());

        for (CustomTableAttributeDTO attr : attributes) {
            sb.append("    Optional<").append(table.getName()).append(" ")
                .append(">findFirstBy")
                .append(MyHelpper.capitalizeFirstLetter(attr.getNameAttribute()))
                .append("(")
                .append(attr.getNameTypeModifier())
                .append(" ")
                .append(MyHelpper.lowerCaseFirstLetter(attr.getNameAttribute()))
                .append(")").
                append(";\n");
        }


        sb.append("}");
        logger.info("Finish Repository generat for ID: " + projectId);
        return sb.toString();
    }
}
