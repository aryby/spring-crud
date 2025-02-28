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
    private  final CustomTableService customTableService;

    public MvcModuleImpl(CustomTableService customTableService) {
        this.customTableService = customTableService;
    }

    @Override
    public String generate(Long projectId) {
        List<CustomTableDTO> allEntities = customTableService.findAllByProjectSetting(projectId);

        // Collect all component names
        List<String> components = new ArrayList<>();
        List<String> services = new ArrayList<>();
        allEntities.forEach(entity -> components.add(MyHelpper.capitalizeFirstLetter(entity.getName()) + "Component"));
        allEntities.forEach(entity -> services.add(MyHelpper.capitalizeFirstLetter(entity.getName()) + "Service"));

        // Start building the module
        StringBuilder sb = new StringBuilder();
        sb.append("""
        import { NgModule } from '@angular/core';
        import { CommonModule } from '@angular/common';
        import { RouterModule } from '@angular/router';
        import { HttpClientModule } from '@angular/common/http';
        import { mvcRoute } from './mvc.routes';

        """);

        // Dynamically import services
        services.forEach(service -> {
            String formattedName = service.replace("Service", "").toLowerCase();
            sb.append(String.format("import { %s } from './services/%s.service';\n", formattedName, formattedName));
        });
        // Dynamically import components
        components.forEach(component -> {
            String formattedName = component.replace("Component", "").toLowerCase();
            sb.append(String.format("import { %s } from './components/%s/%s.component';\n", component, formattedName, formattedName));
        });

        // Define the module with dynamic declarations
        sb.append("""

        @NgModule({
          declarations: [
        """);

        // Add all components to the declarations
        components.forEach(component -> sb.append("    ").append(component).append(",\n"));

        sb.append("""
          ],
          imports: [
              RouterModule.forRoot(mvcRoute, { scrollPositionRestoration: 'enabled' }),
              CommonModule,
              HttpClientModule,
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
