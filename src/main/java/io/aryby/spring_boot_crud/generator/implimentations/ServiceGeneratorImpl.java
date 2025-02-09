package io.aryby.spring_boot_crud.generator.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.IServiceGenerator;
import io.aryby.spring_boot_crud.generator.jpa_generator.*;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.CapitalizeFirstChar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceGeneratorImpl implements IServiceGenerator {

    private Logger logger = LoggerFactory.getLogger(ServiceGeneratorImpl.class);
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;
    private final IJpaGetAll getAll;
    private final IJpaGetById getById;
    private final IJpaSave jpaSave;
    private final IJpaUpdate jpaUpdate;
    private final IJpaDelete jpaDelete;
    // ✅ Constructor Injection
    public ServiceGeneratorImpl(ProjectSettingsRepository projectSettingsRepository,
                                GeneralSettingsRepository generalSettingsRepository,
                                CustomTableAttributeService customTableAttributeService,
                                final IJpaGetAll getAll,
                                final IJpaGetById getById,
                                final  IJpaSave jpaSave,
                                final  IJpaUpdate jpaUpdate,
                                final IJpaDelete jpaDelete) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
        this.getAll=getAll;
        this.getById = getById;
        this.jpaSave=jpaSave;
        this.jpaUpdate=jpaUpdate;
        this.jpaDelete=jpaDelete;
    }

    @Override
    public String generate(CustomTable table, Long projectId) {

        /* getting info*/
        logger.info("Generate Service in precess for : " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));
        logger.info("ProjectSettings found for ID: " + projectId);

        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));
        logger.info("GeneralSettings found for ID: " + projectId);
        /* fin getting infos*/
        // String serviceModifier = CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Service";
        String REPOS_DI = CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Repository";
        String REPO_DI_LOWER = table.getName().toLowerCase() + "repository";
        String packageEntity = generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ".entities";
        String packageRepos = generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ".repositories";
        String packageRequest = generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ".requests";
        String packageDto = generalSettings.getGroupId() + "." + generalSettings.getArtifactId() + ".dtos";
        String ENTITY_MODAL = CapitalizeFirstChar.capitalizeFirstLetter(table.getName());
        String DTO_MODAL = CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "DTO";
        String REPOSITORY_MODEL = CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Repository";
        String SERVICE_MODEL = CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Service";
        String REQUEST_MODEL = CapitalizeFirstChar.capitalizeFirstLetter(table.getName()) + "Request";


        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(generalSettings.getGroupId()).append(".")
            .append(generalSettings.getArtifactId()).append(".services;\n\n");

        // import entity package with model
        sb.append("import "+packageEntity+"."+ENTITY_MODAL+";\n");

        // import dto package with model
        sb.append("import "+packageDto+"."+DTO_MODAL+";\n");

        // import request package with model
        sb.append("import "+packageRequest+"."+REQUEST_MODEL+";\n");

        // import repository package with model
        sb.append("import "+packageRepos+"."+REPOS_DI+";\n\n");

        sb.append("""
            import org.springframework.stereotype.Service;
            import org.springframework.transaction.annotation.Transactional;

            import java.util.List;
            import java.util.stream.Collectors;

            @Service
            """);
        sb.append("public class ").
            append(SERVICE_MODEL).
            append(" {\n");
        // private finale EntityRepository entityRepository;
        sb.append("    private final  ").
            append(REPOS_DI).
            append(" ").
            append(REPO_DI_LOWER).
            append("; \n");

        // DI
        sb.append("\n    public ").
            append(SERVICE_MODEL).
            append("(final ").
            append(REPOS_DI).
            append(" ").
            append(REPO_DI_LOWER).
            append(") {\n ");

        sb.append("     this." + REPO_DI_LOWER + " = " + REPO_DI_LOWER + ";\n" +
            "    }").append("\n\n");
        sb.append("\n");

        // get all
        List<CustomTableAttributeDTO> attributes = customTableAttributeService.findAllByTableId(table.getId());
        sb.append(getAll.getAll(DTO_MODAL, ENTITY_MODAL,REPO_DI_LOWER,attributes));

        // get by id
        sb.append("\n");
        sb.append(getById.getById(DTO_MODAL, ENTITY_MODAL,REPO_DI_LOWER,attributes));
        // create
        sb.append("\n");
        sb.append(jpaSave.create(REQUEST_MODEL,DTO_MODAL, ENTITY_MODAL,REPO_DI_LOWER,attributes));

        sb.append("\n");
        sb.append(jpaUpdate.update(REQUEST_MODEL,DTO_MODAL, ENTITY_MODAL,REPO_DI_LOWER,attributes));
        sb.append("\n");
        sb.append(jpaDelete.deleteById(REPO_DI_LOWER));

        sb.append("\n\n}\n");
        logger.info("Finish Repository generat for ID: " + projectId);
        return sb.toString();
    }
}
