package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.frontend.IAngularServiceGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AngularServiceGeneratorImpl implements IAngularServiceGenerator {
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    public AngularServiceGeneratorImpl(ProjectSettingsRepository projectSettingsRepository, GeneralSettingsRepository generalSettingsRepository, CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generateAngularService(CustomTable table, Long projectId) {
        System.out.println("Generating Service Angular: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        StringBuilder sb = new StringBuilder();

        sb.append("""
            import { Injectable, inject } from '@angular/core';
            import { HttpClient } from '@angular/common/http';
            import { environment } from 'environments/environment';
            """);
        sb.append("import { ");
        sb.append(MyHelpper.capitalizeFirstLetter(table.getName())).append("Entity");
        sb.append(" }");
        sb.append("from '../models/");
        sb.append(table.getName().toLowerCase()).append(".entity';\n");
        sb.append("""

            @Injectable({
              providedIn: 'root',
            })
            """);

        sb.append("export class ").append(MyHelpper.capitalizeFirstLetter(table.getName())).append("Service {\n");

        sb.append("""
                http = inject(HttpClient);

            """);
        sb.append("     apiUrl = environment.apiPath + '/api/" + MyHelpper.capitalizeFirstLetter(table.getName())).append("s';\n\n");

        // get all
        sb.append(" getAll").append(MyHelpper.capitalizeFirstLetter(table.getName()))
            .append("() {\n")
            .append("return this.http.get<" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Entity[]>(this.apiUrl);");
        sb.append("\n\n     }\n");

        // get by id
        sb.append(" get").append(MyHelpper.capitalizeFirstLetter(table.getName()))
            .append("ById(id: any) {\n")
            .append("return this.http.get<" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Entity>(this.apiUrl + '/' + id);");
        sb.append("\n\n     }\n");

        // create
        sb.append(" create").append(MyHelpper.capitalizeFirstLetter(table.getName()))
            .append("(" +MyHelpper.lowerCaseFirstLetter(table.getName())+" : "+ MyHelpper.capitalizeFirstLetter(table.getName()) + "Entity) {\n")
            .append("return this.http.post<" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Entity>(this.apiUrl + '/' + " + MyHelpper.lowerCaseFirstLetter(table.getName()) + ");");
        sb.append("\n\n     }\n");

        // update
        sb.append("update").append(MyHelpper.capitalizeFirstLetter(table.getName()))
            .append("(id: any, " + table.getName().toLowerCase() +" : "+ MyHelpper.capitalizeFirstLetter(table.getName()) + "Entity) {\n")
            .append("return this.http.put<" + MyHelpper.capitalizeFirstLetter(table.getName()) + "Entity>(this.apiUrl + '/id, ' + " + table.getName().toLowerCase()+ ");");
        sb.append("\n\n     }\n");

        // delete
        sb.append("delete").append(MyHelpper.capitalizeFirstLetter(table.getName()))
            .append("ById(id: any) {\n")
            .append("return this.http.delete(this.apiUrl + '/' + id);");
        sb.append("\n\n     }\n");

        sb.append("}\n");

        return sb.toString();
    }
}
