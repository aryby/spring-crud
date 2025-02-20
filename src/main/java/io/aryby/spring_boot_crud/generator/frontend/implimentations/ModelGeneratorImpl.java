package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.frontend.IModelGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelGeneratorImpl implements IModelGenerator {

    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    public ModelGeneratorImpl(ProjectSettingsRepository projectSettingsRepository, GeneralSettingsRepository generalSettingsRepository, CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generateAngularModel(CustomTable table, Long projectId) {
        System.out.println("Generating Model Angular: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        StringBuilder sb = new StringBuilder();

        sb.append("export class ").append(MyHelpper.capitalizeFirstLetter(table.getName())).append("Entity {\n");

        sb.append("constructor(data:Partial<").append(MyHelpper.capitalizeFirstLetter(table.getName())).append("Entity>) {\n").append("""
                    Object.assign(this, data);
                    }

                    id?: any;
                """);

        List<CustomTableAttributeDTO> attributes = customTableAttributeService.findAllByTableId(table.getId());

        for (CustomTableAttributeDTO attr : attributes) {

                sb.append(attr.getNameAttribute()).append("?: ")
                    .append(attr.getNameTypeModifier().toLowerCase()).append(";\n");
        }

        sb.append("""
                    dateCreated?:any;
                    lastUpdated?:any;
                """);

        sb.append("}");

        return sb.toString();
    }
}
