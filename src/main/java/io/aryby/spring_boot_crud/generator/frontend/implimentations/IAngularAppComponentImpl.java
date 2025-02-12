package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.generator.frontend.IAngularAppComponent;
import org.springframework.stereotype.Service;

@Service
public class IAngularAppComponentImpl implements IAngularAppComponent {
    @Override
    public String generateAngularAppComponent(Long projectId) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
            // src/app/app.component.ts
            import { Component } from '@angular/core';

            @Component({
              selector: 'app-root',
              template: `<h1>Welcome to Angular</h1>`,
              styles: [`h1 { color: blue; }`]
            })
            export class AppComponent {}


            """);

        return sb.toString();
    }
}
