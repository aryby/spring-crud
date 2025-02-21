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
        System.out.println("Generating Components Angular: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        GeneralSettings generalSettings = generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        StringBuilder sb = new StringBuilder();

        String tableFormatedName = table.getName().toLowerCase();


        sb.append("""
            import { Component, inject, OnDestroy, OnInit } from '@angular/core';
            import { NavigationEnd, Router, RouterLink } from '@angular/router';
            import { Subscription } from 'rxjs';\n
            """);
        sb.append("\nimport { ");
        sb.append(MyHelpper.capitalizeFirstLetter(table.getName())).append("Entity");
        sb.append(" }");
        sb.append("from '../../models/");
        sb.append(table.getName().toLowerCase()).append(".entity';\n");

        sb.append("import { ");
        sb.append(MyHelpper.capitalizeFirstLetter(table.getName())).append("Service");
        sb.append(" }");
        sb.append("from '../../services/");
        sb.append(table.getName().toLowerCase()).append(".service';\n");

        sb.append("""
                  @Component({
                    standalone:true,
                    selector: 'app-%s-list',
                  """.formatted(tableFormatedName));
        sb.append("       templateUrl: './%s-list.component.html,'\n})\n".formatted(tableFormatedName));


        sb.append("export class ").append(MyHelpper.capitalizeFirstLetter(table.getName())).append("Component implements OnInit, OnDestroy  {\n");

        sb.append("\n     "+MyHelpper.lowerCaseFirstLetter(table.getName())).append("Service = inject(");
        sb.append(MyHelpper.capitalizeFirstLetter(table.getName())).append("Service);\n");

        sb.append("     router = inject(Router);\n");
        sb.append("     "+MyHelpper.lowerCaseFirstLetter(table.getName())).append("Entity?: ");
        sb.append(MyHelpper.capitalizeFirstLetter(table.getName())).append("Entity[] = []; \n");
        sb.append("     navigationSubscription?: Subscription;\n\n");
        // get all
        sb.append("""
                 ngOnInit() {
                        this.loadData();
                        this.navigationSubscription = this.router.events.subscribe((event) => {
                            if (event instanceof NavigationEnd) {
                                this.loadData();
                            }
                        });
                    }
            """);

        sb.append("\n\n\n");

        sb.append("     loadData() {\n");
        sb.append("         this."+MyHelpper.lowerCaseFirstLetter(table.getName())).append("Service.getAll").append(MyHelpper.capitalizeFirstLetter(table.getName()))
            .append("().subscribe({\n");
        sb.append("""
                                next: (data) => (this.%sEntity = data),
                  """.formatted(MyHelpper.lowerCaseFirstLetter(table.getName())));
        sb.append("""
                                error: (error) => console.log(error),
                  """);
        sb.append("""
                    });
                }
            \n""");

        sb.append("""
                ngOnDestroy() {
                    this.navigationSubscription!.unsubscribe();
                }
            """);

        sb.append("}\n");

        return sb.toString();
    }

    @Override
    public String generateAngularComponentsHtml(CustomTable table, Long projectId) {
        return "<h1>Angular Components</h1>\n";
    }
}
