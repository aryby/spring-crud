package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.frontend.IAngularComponentsGenerator;
import io.aryby.spring_boot_crud.generator.frontend.IMvcComponentCreateTs;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

@Service
public class MvcAngularComponentsCreateTsImpl implements IMvcComponentCreateTs {
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    public MvcAngularComponentsCreateTsImpl(ProjectSettingsRepository projectSettingsRepository, GeneralSettingsRepository generalSettingsRepository, CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generate(CustomTable table, Long projectId) {
        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        String tableName = table.getName();
        String formattedTableName = tableName.toLowerCase();
        String capitalizedTableName = MyHelpper.capitalizeFirstLetter(tableName);
        String lowerCaseTableName = MyHelpper.lowerCaseFirstLetter(tableName);

        StringBuilder sb = new StringBuilder();

        // ✅ 1. Import statements
        sb.append("""
    import { Component, inject, OnInit } from '@angular/core';
    import { Router } from '@angular/router';
    import { %sEntity } from '../../models/%s.entity';
    import { %sService } from '../../services/%s.service';
    """.formatted(capitalizedTableName, formattedTableName, capitalizedTableName, formattedTableName));

        // ✅ 2. Component decorator
        sb.append("""
    @Component({
        selector: 'app-%s-add',
        templateUrl: './%s-add.component.html',
        standalone: false,
    })
    """.formatted(formattedTableName, formattedTableName));

        // ✅ 3. Class declaration with model
        sb.append("""
    export class %sAddComponent implements OnInit {

        %sService = inject(%sService);
        router = inject(Router);

        %s: %sEntity={ }; // 🟢 model مرتبط بـ ngModel

        ngOnInit() { }

        create() {
            this.%sService.create%s(this.%s).subscribe({
                next: (data) => {
                    console.log('Created:', data);
                    this.router.navigate(['/%s']); // ✅ توجيه بعد الحفظ
                },
                error: (error) => console.error(error),
            });
        }
    }
    """.formatted(
            capitalizedTableName,
            lowerCaseTableName, capitalizedTableName,
            lowerCaseTableName, capitalizedTableName, capitalizedTableName,
            lowerCaseTableName, capitalizedTableName, lowerCaseTableName,
            formattedTableName
        ));

        return sb.toString();
    }


}
