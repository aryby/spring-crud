package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IControllerGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ControllerGeneratorImpl implements IControllerGenerator {

    private final Logger logger = LoggerFactory.getLogger(ControllerGeneratorImpl.class);
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    // Constructor Injection
    public ControllerGeneratorImpl(ProjectSettingsRepository projectSettingsRepository,
                                   GeneralSettingsRepository generalSettingsRepository,
                                   CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generate(CustomTable table, Long projectId) {

        // Fetch project and general settings
        logger.info("Generate Controller in process ...");

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        logger.info("GeneralSettings found for ID: " + projectId);

        // Define class and package names
        String controllerName = MyHelpper.capitalizeFirstLetter(table.getName()) + "Controller";
        String serviceName = MyHelpper.capitalizeFirstLetter(table.getName()) + "Service";
        String serviceVariable = table.getName().toLowerCase() + "Service";
        String dtoName = MyHelpper.capitalizeFirstLetter(table.getName()) + "DTO";
        String requestDto = MyHelpper.capitalizeFirstLetter(table.getName()) + "Request";
        String entityName = MyHelpper.capitalizeFirstLetter(table.getName());

        String packageEntity = generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ".entities";
        String packageService = generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ".services";
        String packageDto = generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ".dtos";
        String packageRequest = generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ".requests";

        StringBuilder sb = new StringBuilder();

        // Package definition
        sb.append("package ").append(generalSettings.getGroupId()).append(".")
            .append(generalSettings.getArtifactId()).append(".controllers;\n\n");

        // Imports
        sb.append("import ").append(packageEntity).append(".").append(entityName).append(";\n")
            .append("import ").append(packageDto).append(".").append(dtoName).append(";\n")
            .append("import ").append(packageRequest).append(".").append(requestDto).append(";\n")
            .append("import ").append(packageService).append(".").append(serviceName).append(";\n\n");

        sb.append("""
            import jakarta.validation.Valid;
            import java.util.List;
            import org.springframework.http.HttpStatus;
            import org.springframework.http.ResponseEntity;
            import org.springframework.web.bind.annotation.*;


            @RestController
            @CrossOrigin(value = "*")
            """)
            .append("@RequestMapping(value = \"/api/")
            .append(entityName.toLowerCase()).append("s\")\n");

        sb.append("public class ").append(controllerName).append(" {\n");

        // Service dependency injection
        sb.append("    private final ").append(serviceName).append(" ").append(serviceVariable).append(";\n\n");

        sb.append("    public ").append(controllerName).append("(")
            .append("final ").append(serviceName).append(" ").append(serviceVariable).append(") {\n")
            .append("        this.").append(serviceVariable).append(" = ").append(serviceVariable).append(";\n")
            .append("    }\n\n");

        // GET ALL
        sb.append("    @GetMapping\n")
            .append("    public ResponseEntity<List<").append(dtoName).append(">> getAll")
            .append(entityName).append("s() {\n")
            .append("        return ResponseEntity.ok(").append(serviceVariable).append(".getAll")
            .append(entityName).append("s());\n")
            .append("    }\n\n");

        // GET BY ID
        sb.append("    @GetMapping(\"/{id}\")\n")
            .append("    public ResponseEntity<").append(dtoName).append("> get")
            .append(entityName).append("ById(@PathVariable Long id) {\n")
            .append("        return ResponseEntity.ok(").append(serviceVariable)
            .append(".getById").append(entityName).append("(id));\n")
            .append("    }\n\n");

        // CREATE
        sb.append("    @PostMapping\n")
            .append("    public ResponseEntity<").append(dtoName).append("> create")
            .append(entityName).append("(@Valid @RequestBody ").append(requestDto).append(" request) {\n")
            .append("        return new ResponseEntity<>(").append(serviceVariable)
            .append(".create").append(entityName).append("(request), HttpStatus.CREATED);\n")
            .append("    }\n\n");

        // UPDATE
        sb.append("    @PutMapping(\"/{id}\")\n")
            .append("    public ResponseEntity<").append(dtoName).append("> update")
            .append(entityName).append("(@PathVariable Long id, @Valid @RequestBody ")
            .append(requestDto).append(" request) {\n")
            .append("        return ResponseEntity.ok(").append(serviceVariable)
            .append(".update").append(entityName).append("(id, request));\n")
            .append("    }\n\n");

        // DELETE
        sb.append("    @DeleteMapping(\"/{id}\")\n")
            .append("    public ResponseEntity<Void> delete")
            .append(entityName).append("(@PathVariable Long id) {\n")
            .append("        ").append(serviceVariable).append(".deleteById(id);\n")
            .append("        return ResponseEntity.noContent().build();\n")
            .append("    }\n");

        sb.append("}\n");

        logger.info("Finish Controller generation for ID: " + projectId);
        return sb.toString();
    }
}
