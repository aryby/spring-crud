package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.generator.frontend.IAngularAppModuleRoutingTs;
import org.springframework.stereotype.Service;

@Service
public class IAngularAppModuleRoutingTsImpl implements IAngularAppModuleRoutingTs {
    @Override
    public String generateAngularAppModuleTs(Long projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
            import { NgModule } from '@angular/core';
            import { RouterModule, Routes } from '@angular/router';

            const routes: Routes = [];

            @NgModule({
              imports: [RouterModule.forRoot(routes)],
              exports: [RouterModule]
            })
            export class AppRoutingModule { }


            """);

        return sb.toString();
    }
}
