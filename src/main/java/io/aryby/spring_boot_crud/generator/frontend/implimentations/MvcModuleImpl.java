package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableDTO;
import io.aryby.spring_boot_crud.custom_table.CustomTableService;
import io.aryby.spring_boot_crud.generator.frontend.IMvcModule;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MvcModuleImpl implements IMvcModule {
    private final CustomTableService customTableService;

    public MvcModuleImpl(CustomTableService customTableService) {
        this.customTableService = customTableService;
    }

    @Override
    public String generate(Long projectId) {
        List<CustomTableDTO> allEntities = customTableService.findAllByProjectSetting(projectId);

        // Collect all component names
        List<String> listComponents = new ArrayList<>();
        allEntities.forEach(entity -> listComponents.add(MyHelpper.capitalizeFirstLetter(entity.getName()) + "ListComponent"));

        List<String> addComponents = new ArrayList<>();
        allEntities.forEach(entity -> addComponents.add(MyHelpper.capitalizeFirstLetter(entity.getName()) + "AddComponent"));

        // services
        List<String> services = new ArrayList<>();
        allEntities.forEach(entity -> services.add(MyHelpper.capitalizeFirstLetter(entity.getName()) + "Service"));

        // Start building the module
        StringBuilder sb = new StringBuilder();
        sb.append("""
            import { NgModule } from '@angular/core';
            import { CommonModule } from '@angular/common';
            import { RouterModule } from '@angular/router';
            import { HttpClientModule } from '@angular/common/http';
            import { mvcRoute } from './mvc.routes';
            import { FormsModule, ReactiveFormsModule } from '@angular/forms';
            """);


        // Dynamically import components
        listComponents.forEach(component -> {
            String nomEntity = component.replace("ListComponent", "").toLowerCase();
            String addImportEntity = component.replace("ListComponent", "AddComponent");

            String nomListComponent = nomEntity + "-list";
            String nomAddComponent = nomEntity + "-add";
            sb.append(String.format("import { %s } from './components/%s/%s.component';\n", component, nomEntity, nomListComponent));
            sb.append(String.format("import { %s } from './components/%s/%s.component';\n", addImportEntity, nomEntity, nomAddComponent));

        });


        // Dynamically import services
        services.forEach(service -> {
            String formattedName = service.replace("Service", "").toLowerCase();
            sb.append(String.format("import { %s } from './services/%s.service';\n", service, formattedName));
        });
        // Define the module with dynamic declarations
        sb.append("""

            @NgModule({
              declarations: [
            """);

        // Add all components to the declarations
        listComponents.forEach(component -> sb.append("    ").append(component).append(",\n"));
        addComponents.forEach(component -> sb.append("    ").append(component).append(",\n"));

        sb.append("""
            ],
            imports: [
                RouterModule.forRoot(mvcRoute, { scrollPositionRestoration: 'enabled' }),
                CommonModule,
                HttpClientModule,
                ReactiveFormsModule,
                FormsModule
            ],
                providers: [
            """);
        services.forEach(service -> sb.append("    ").append(service).append(",\n"));
        sb.append("""
            ]
            })
            export class MvcModule { }
            """);

        return sb.toString();
    }

}
