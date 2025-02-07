package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IControllerGenerator;
import io.aryby.spring_boot_crud.generator.IRepositoryGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.CapitalizeFirstChar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class ControllerGeneratorImpl implements IControllerGenerator {

    private Logger logger = LoggerFactory.getLogger(ControllerGeneratorImpl.class);
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    // ✅ Constructor Injection
    public ControllerGeneratorImpl(ProjectSettingsRepository projectSettingsRepository,
                                   GeneralSettingsRepository generalSettingsRepository,
                                   CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generate(CustomTable table, Long projectId) {
        logger.info("Generate Controller in precess ...");
        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));
        logger.info("ProjectSettings found for ID: " + projectId);
        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));
        logger.info("GeneralSettings found for ID: " + projectId);

        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(generalSettings.getGroupId()).append(".")
            .append(generalSettings.getArtifactId()).append(".controllers;\n\n");

        String controllerFormatedName = CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Controller";
        String repoFormatedName = CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Repository";


        sb.append("""
            import io.aryby.spring_boot_crud.util.ReferencedException;
            import io.aryby.spring_boot_crud.util.ReferencedWarning;
            import io.swagger.v3.oas.annotations.responses.ApiResponse;
            import jakarta.validation.Valid;
            import java.util.List;
            import org.springframework.http.HttpStatus;
            import org.springframework.http.MediaType;
            import org.springframework.http.ResponseEntity;
            import org.springframework.web.bind.annotation.*;


            @RestController
            @CrossOrigin("*")
            """);
        sb.append("@RequestMapping(value = \"/api/");
        sb.append(table.getName().toLowerCase()).append("\")\n");
        sb.append("public class ").
            append(controllerFormatedName).
            append(" {\n");
            // private finale EntityRepository entityRepository;
        sb.append("    private finale  ").
            append(repoFormatedName).
            append(" \n");
        // public EntityController(EntityRepository entityRepository)
        // {
        //      this.entityRepository = entityRepository
        // }
        sb.append("\n    public ").
            append(controllerFormatedName).
            append("(final ").
            append(repoFormatedName).
            append(" ").
            append(table.getName().toLowerCase()).
            append("Repository) {\n ");

        sb.append("     this."
            + table.getName().toLowerCase() + "Repository = "
            + table.getName().toLowerCase() + "Repository;\n" +
            "    }").append("\n\n");

        sb.append("    @GetMapping\n" +
            "    public ResponseEntity<List<table.getName()>> getAll"+table.getName()+"() {\n" +
            "           return ResponseEntity.ok("+ table.getName().toLowerCase() + "Repository.findAll());\n" +
            "    }\n");
        sb.append("}\n");
        logger.info("Finish Repository generat for ID: " + projectId);
        return sb.toString();
    }
}
