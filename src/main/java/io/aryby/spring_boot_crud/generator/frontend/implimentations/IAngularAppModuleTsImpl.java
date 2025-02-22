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
            // generated from generateAngularAppModuleTs
            import { NgModule } from '@angular/core';
            import { BrowserModule, Title } from '@angular/platform-browser';
            import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
            import { CommonModule } from '@angular/common';
            import { FormsModule, ReactiveFormsModule } from '@angular/forms';
            import { HttpBackend, HttpClient, HttpClientModule } from '@angular/common/http';
            import { RouterModule } from '@angular/router';

            //Routes
            import { routes } from './app.route';

            import { AppComponent } from './app.component';

            // store
            import { StoreModule } from '@ngrx/store';
            import { indexReducer } from './store/index.reducer';

            // shared module
            import { SharedModule } from 'src/shared.module';

            // i18n
            import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
            import { TranslateHttpLoader } from '@ngx-translate/http-loader';
            // AOT compilation support
            export function HttpLoaderFactory(httpHandler: HttpBackend): TranslateHttpLoader {
                return new TranslateHttpLoader(new HttpClient(httpHandler));
            }

            // dashboard
            import { IndexComponent } from './index';

            // Layouts
            import { AppLayout } from './layouts/app-layout';
            import { AuthLayout } from './layouts/auth-layout';

            import { HeaderComponent } from './layouts/header';
            import { FooterComponent } from './layouts/footer';
            import { SidebarComponent } from './layouts/sidebar';
            import { ThemeCustomizerComponent } from './layouts/theme-customizer';
            import { MvcModule } from './mvc/mvc.module';

            @NgModule({
                imports: [
                    RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' }),
                    BrowserModule,
                    BrowserAnimationsModule,
                    CommonModule,
                    FormsModule,
                    HttpClientModule,
                    TranslateModule.forRoot({
                        loader: {
                            provide: TranslateLoader,
                            useFactory: HttpLoaderFactory,
                            deps: [HttpBackend],
                        },
                    }),
                    StoreModule.forRoot({ index: indexReducer }),
                    SharedModule.forRoot(),
                    MvcModule,
                ],
                declarations: [
                    AppComponent,\s
                    HeaderComponent,\s
                    FooterComponent, SidebarComponent,\s
                    ThemeCustomizerComponent,\s
                    IndexComponent, AppLayout,\s
                    AuthLayout],
                providers: [Title],
                bootstrap: [AppComponent],
            })
            export class AppModule {}

            """);

        return sb.toString();
    }
}
