package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTableDTO;
import io.aryby.spring_boot_crud.custom_table.CustomTableService;
import io.aryby.spring_boot_crud.generator.frontend.IAngularAppModuleTs;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IAngularAppModuleTsImpl implements IAngularAppModuleTs {

    private final CustomTableService customTableService;

    public IAngularAppModuleTsImpl(CustomTableService customTableService) {
        this.customTableService = customTableService;
    }

    @Override
    public String generateAngularAppModuleTs(Long projectId) {
        List<CustomTableDTO> allEntities = customTableService.findAllByProjectSetting(projectId);

        List<String> components = new ArrayList<>();
        allEntities.forEach(entity -> {
            components.add(entity.getName());
        });

        StringBuilder sb = new StringBuilder();
        sb.append("""
            import { NgModule } from '@angular/core';
            import { BrowserModule } from '@angular/platform-browser';

            import { AppRoutingModule } from './app-routing.module';
            import { AppComponent } from './app.component';
            import {MvcModule} from './mvc/mvc.module';

            @NgModule({
              declarations: [
                AppComponent
              ],
              imports: [
                BrowserModule,
                AppRoutingModule,
                MvcModule
              ],
              providers: [],
              bootstrap: [AppComponent]
            })
            export class AppModule { }

            """);

        return sb.toString();
    }
}
