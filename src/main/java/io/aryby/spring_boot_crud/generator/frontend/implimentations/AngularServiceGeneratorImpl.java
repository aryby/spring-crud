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
        System.out.println("Generating Angular Service for: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        String entityName = MyHelpper.capitalizeFirstLetter(table.getName());
        String entityVar = MyHelpper.lowerCaseFirstLetter(table.getName());
        String apiUrl = "environment.apiPath + '/api/" + entityName + "s'";

        return String.format("""
        import { Injectable, inject } from '@angular/core';
        import { HttpClient } from '@angular/common/http';
        import { environment } from '../../../environments/environment';
        import { %1$sEntity } from '../models/%2$s.entity';

        @Injectable({ providedIn: 'root' })
        export class %1$sService {
            http = inject(HttpClient);
            apiUrl = %3$s;

            getAll%1$s() {
                return this.http.get<%1$sEntity[]>(this.apiUrl);
            }

            get%1$sById(id: any) {
                return this.http.get<%1$sEntity>(`${this.apiUrl}/${id}`);
            }

            create%1$s(%4$s: %1$sEntity) {
                return this.http.post<%1$sEntity>(this.apiUrl, %4$s);
            }

            update%1$s(id: any, %2$s: %1$sEntity) {
                return this.http.put<%1$sEntity>(`${this.apiUrl}/${id}`, %2$s);
            }

            delete%1$sById(id: any) {
                return this.http.delete(`${this.apiUrl}/${id}`);
            }
        }
        """, entityName, table.getName().toLowerCase(), apiUrl, entityVar
        );
    }

}
