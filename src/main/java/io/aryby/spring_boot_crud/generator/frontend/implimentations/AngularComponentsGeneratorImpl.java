package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.frontend.IAngularComponentsGenerator;
import io.aryby.spring_boot_crud.generator.frontend.IAngularServiceGenerator;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

@Service
public class AngularComponentsGeneratorImpl implements IAngularComponentsGenerator {
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    public AngularComponentsGeneratorImpl(ProjectSettingsRepository projectSettingsRepository, GeneralSettingsRepository generalSettingsRepository, CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generateAngularComponentsTs(CustomTable table, Long projectId) {
        // Fetch Project and General Settings
        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        // Prepare formatted names
        String tableName = table.getName();
        String formattedTableName = tableName.toLowerCase();
        String capitalizedTableName = MyHelpper.capitalizeFirstLetter(tableName);
        String lowerCaseTableName = MyHelpper.lowerCaseFirstLetter(tableName);

        // Generate component content
        StringBuilder sb = new StringBuilder();

        // Import statements
        sb.append("""
        import { Component, inject, OnDestroy, OnInit } from '@angular/core';
        import { NavigationEnd, Router } from '@angular/router';
        import { Subscription } from 'rxjs';

        import { %sEntity } from '../../models/%s.entity';
        import { %sService } from '../../services/%s.service';
        """.formatted(capitalizedTableName, formattedTableName, capitalizedTableName, formattedTableName));

        // Component decorator
        sb.append("""
        @Component({
            selector: 'app-%s-list',
            templateUrl: './%s-list.component.html',
            standalone: false,
        })
        """.formatted(formattedTableName, formattedTableName));

        // Class declaration
        sb.append("""
        export class %sListComponent implements OnInit, OnDestroy {
            %sService = inject(%sService);
            router = inject(Router);
            %sEntity?: %sEntity[] = [];
            navigationSubscription?: Subscription;

            ngOnInit() {
                this.loadData();
                this.navigationSubscription = this.router.events.subscribe((event) => {
                    if (event instanceof NavigationEnd) {
                        this.loadData();
                    }
                });
            }

            loadData() {
                this.%sService.getAll%s().subscribe({
                    next: (data) => (this.%sEntity = data),
                    error: (error) => console.log(error),
                });
            }

            confirmDelete(id:any){
                console.log(id);
            }

            ngOnDestroy() {
                this.navigationSubscription?.unsubscribe();
            }
        }
        """.formatted(
                capitalizedTableName,
                lowerCaseTableName, capitalizedTableName,
                lowerCaseTableName, capitalizedTableName,
                lowerCaseTableName, capitalizedTableName, lowerCaseTableName
            )
        );

        return sb.toString();
    }


    @Override
    public String generateAngularComponentsHtml(CustomTable table, Long projectId) {
        return "<h1>Angular Components</h1>\n";
    }
}
