package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.generator.frontend.IAngularAppModuleTs;
import io.aryby.spring_boot_crud.generator.frontend.IPackageJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IPackageJsonImpl implements IPackageJson {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Override
    public String generatePackageJson(Long projectId) {
        logger.info("Generate Angular package.json file {}", projectId);

        StringBuilder sb = new StringBuilder();


        sb.append("""
            {
                "name": "angular-generator",
                "version": "0.0.0",
                "description": "Spring Boot Angular generator",
                "author": "Aryby",
                "contact": "aryby00@gmail.com",
                "scripts": {
                    "ng": "ng",
                    "start": "ng serve --open",
                    "build": "ng build",
                    "watch": "ng build --watch --configuration development",
                    "test": "ng test"
                },
                "private": true,
                "dependencies": {
                    "@angular/animations": "^18.2.0",
                    "@angular/cdk": "^18.2.0",
                    "@angular/common": "^18.2.0",
                    "@angular/compiler": "^18.2.0",
                    "@angular/core": "^18.2.0",
                    "@angular/forms": "^18.2.0",
                    "@angular/platform-browser": "^18.2.0",
                    "@angular/platform-browser-dynamic": "^18.2.0",
                    "@angular/router": "^18.2.0",
                    "@ngrx/store": "^18.0.2",
                    "@ngx-translate/core": "^15.0.0",
                    "@ngx-translate/http-loader": "^8.0.0",
                    "headlessui-angular": "^0.0.9",
                    "http-status-codes": "^2.3.0",
                    "ngx-custom-modal": "^18.0.2",
                    "ngx-scrollbar": "^15.1.2",
                    "rxjs": "~7.8.0",
                    "tslib": "^2.5.2",
                    "zone.js": "~0.14.4"
                },
                "devDependencies": {
                    "@angular-devkit/build-angular": "^18.2.0",
                    "@angular/cli": "^18.2.0",
                    "@angular/compiler-cli": "^18.2.0",
                    "@angular/localize": "^18.2.0",
                    "@tailwindcss/forms": "^0.5.3",
                    "@tailwindcss/typography": "^0.5.9",
                    "@types/jasmine": "~5.1.4",
                    "@types/node": "^22.4.0",
                    "autoprefixer": "^10.4.16",
                    "jasmine-core": "~5.2.0",
                    "karma": "~6.4.2",
                    "karma-chrome-launcher": "~3.2.0",
                    "karma-coverage": "~2.2.0",
                    "karma-jasmine": "~5.1.0",
                    "karma-jasmine-html-reporter": "^2.1.0",
                    "postcss": "^8.4.24",
                    "prettier": "^3.3.3",
                    "prettier-plugin-tailwindcss": "^0.6.6",
                    "tailwindcss": "^3.4.10",
                    "typescript": "^5.5.4"
                }
            }

            """);

        return sb.toString();
    }

    public static class IAngularAppModuleTsImpl implements IAngularAppModuleTs {
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
}
