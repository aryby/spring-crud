package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTableDTO;
import io.aryby.spring_boot_crud.custom_table.CustomTableService;
import io.aryby.spring_boot_crud.generator.frontend.IMvcRoutes;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MvcRoutesImpl implements IMvcRoutes {
    private  final CustomTableService customTableService;

    public MvcRoutesImpl(CustomTableService customTableService) {
        this.customTableService = customTableService;
    }

    @Override
    public String generate(Long projectId) {
        List<CustomTableDTO> allEntities = customTableService.findAllByProjectSetting(projectId);

        // Collect all component names
        List<String> components = new ArrayList<>();
        allEntities.forEach(entity -> components.add(MyHelpper.capitalizeFirstLetter(entity.getName()) + "Component"));

        // Start building the routes file
        StringBuilder sb = new StringBuilder();

        // Base imports
        sb.append("""
        import { Routes } from '@angular/router';
        import { AppLayout } from '../layouts/app-layout';
        import { AuthLayout } from '../layouts/auth-layout';
        """);

        // Dynamically import components
        components.forEach(component -> {
            String formattedName = component.replace("Component", "").toLowerCase();
            sb.append(String.format("import { %s } from '../components/%s/%s.component';\n", component, formattedName, formattedName));
        });

        // Define routes
        sb.append("""

        export const mvcRoute: Routes = [
            {
                path: '',
                component: AppLayout,
                children: [
        """);

        // Add routes for each component dynamically
        components.forEach(component -> {
            String pathName = component.replace("Component", "").toLowerCase();
            sb.append(String.format("                    { path: '%s', component: %s, data: { title: '%s' } },\n",
                pathName, component, MyHelpper.capitalizeFirstLetter(pathName)));
        });

        // Close AppLayout and AuthLayout routes
        sb.append("""
                ],
            },
            {
                path: '',
                component: AuthLayout,
                children: [],
            },
        ];
        """);

        return sb.toString();
    }

}
