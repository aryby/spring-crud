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

        List<String> components = new ArrayList<>();
        allEntities.forEach(entity -> {
            components.add(MyHelpper.capitalizeFirstLetter(entity.getName()) + "ListComponent");
            components.add(MyHelpper.capitalizeFirstLetter(entity.getName()) + "AddComponent");
        });

        StringBuilder sb = new StringBuilder();

        // Base imports
        sb.append("""
                import { Routes } from '@angular/router';

                """);

        // Dynamically import components
        allEntities.forEach(entity -> {
            String formattedName = entity.getName().toLowerCase();
            sb.append(String.format("import { %sListComponent } from './components/%s/%s-list.component';\n",
                MyHelpper.capitalizeFirstLetter(formattedName),
                formattedName,
                formattedName));
            sb.append(String.format("import { %sAddComponent } from './components/%s/%s-add.component';\n",
                MyHelpper.capitalizeFirstLetter(formattedName),
                formattedName,
                formattedName));
        });

        // Define routes
        sb.append("""

    export const mvcRoute: Routes = [
    """);

        // Add routes for each entity dynamically
        allEntities.forEach(entity -> {
            String pathName = entity.getName().toLowerCase();
            sb.append(String.format("""
              { path: '%s', children: [
                  { path: '', component: %sListComponent, data: { title: '%s' } },
                  { path: 'add', component: %sAddComponent, data: { title: 'Ajouter %s' } }
              ] },
        """,
                pathName,
                MyHelpper.capitalizeFirstLetter(pathName),
                MyHelpper.capitalizeFirstLetter(pathName),
                MyHelpper.capitalizeFirstLetter(pathName),
                MyHelpper.capitalizeFirstLetter(pathName)));
        });

        sb.append("""
    ];
    """);

        return sb.toString();
    }

}
