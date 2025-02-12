package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.generator.frontend.IAngularMainTs;
import org.springframework.stereotype.Service;

@Service
public class IAngularMainTsImpl implements IAngularMainTs {
    @Override
    public String generateAngularMainTs(Long projectId) {
        StringBuilder sb=new StringBuilder();
        sb.append("""
            /// <reference types="@angular/localize" />

            import { enableProdMode } from '@angular/core';
            import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

            import { AppModule } from './app/app.module';
            import { environment } from './environments/environment';

            export function getBaseUrl() {
                return document.getElementsByTagName('base')[0].href;
            }

            const providers = [{ provide: 'BASE_URL', useFactory: getBaseUrl, deps: [] }];

            if (environment.production) {
                enableProdMode();
            }

            platformBrowserDynamic(providers)
                .bootstrapModule(AppModule)
                .catch((err) => console.log(err));

            """);
        return sb.toString();
    }
}
