package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.frontend.IMvcComponentCreate;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MvcComponentCreateImpl implements IMvcComponentCreate {

    private final Logger logger = LoggerFactory.getLogger(MvcComponentCreateImpl.class);
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    public MvcComponentCreateImpl(ProjectSettingsRepository projectSettingsRepository, GeneralSettingsRepository generalSettingsRepository, CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generate(CustomTable table, Long projectId) {

        logger.info("Generate Angular Create Component for: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));
        generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        String entityName = MyHelpper.capitalizeFirstLetter(table.getName());
        String lowerEntityName = MyHelpper.lowerCaseFirstLetter(table.getName());
        List<CustomTableAttributeDTO> attributes = customTableAttributeService.findAllByTableId(table.getId());

        StringBuilder sb = new StringBuilder();

        // Angular Template for List Component
        sb.append(String.format("""

        <div class="overflow-x-auto">
            <h2 class="text-2xl font-bold mb-4">%1$s + </h2>
            <form>
        """, lowerEntityName));

        // Generate dynamic table headers
        attributes.forEach(attr -> sb.append(String.format("""
                <input type="text" name="%s"></input>

        """, MyHelpper.capitalizeFirstLetter(attr.getNameAttribute()))));

        // Add Edit and Delete Actions
        sb.append("""
            </form>
        </div>
        """);

        return sb.toString();
    }



}
