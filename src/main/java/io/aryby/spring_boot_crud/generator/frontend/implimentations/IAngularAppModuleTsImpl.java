package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.generator.frontend.IAngularAppModuleTs;
import org.springframework.stereotype.Service;

@Service
public class IAngularAppModuleTsImpl implements IAngularAppModuleTs {
    @Override
    public String generateAngularAppModuleTs(Long projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
            // src/app/app.module.ts
            import { NgModule } from '@angular/core';
            import { BrowserModule } from '@angular/platform-browser';
            import { AppRoutingModule } from './app-routing.module';
            import { AppComponent } from './app.component';

            @NgModule({
              declarations: [AppComponent], // Declare components here
              imports: [BrowserModule, AppRoutingModule], // Import modules
              providers: [], // Services (later)
              bootstrap: [AppComponent] // Entry component
            })
            export class AppModule {}

            """);

        return sb.toString();
    }
}
