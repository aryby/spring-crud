package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.generator.frontend.IAngularAppModuleRoutingTs;
import org.springframework.stereotype.Service;

@Service
public class IAngularAppModuleRoutingTsImpl implements IAngularAppModuleRoutingTs {
    @Override
    public String generateAngularAppModuleTs(Long projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
            // src/app/app-routing.module.ts
            import { NgModule } from '@angular/core';
            import { RouterModule, Routes } from '@angular/router';
            import { AppComponent } from './app.component';

            const routes: Routes = [
              { path: '', component: AppComponent }, // Default route
            ];

            @NgModule({
              imports: [RouterModule.forRoot(routes)],
              exports: [RouterModule]
            })
            export class AppRoutingModule {}


            """);

        return sb.toString();
    }
}
