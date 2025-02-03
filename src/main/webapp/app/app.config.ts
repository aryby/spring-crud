import { HttpClientModule } from '@angular/common/http';
import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule, ExtraOptions, TitleStrategy } from '@angular/router';
import { routes } from 'app/app.routes';
import { CustomTitleStrategy } from 'app/common/title-strategy.injectable';
import {CommonModule} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";


const routeConfig: ExtraOptions = {
  onSameUrlNavigation: 'reload',
  scrollPositionRestoration: 'enabled'
};

export const appConfig: ApplicationConfig = {
  providers: [
    importProvidersFrom(
      RouterModule.forRoot(routes, routeConfig),
      BrowserAnimationsModule,
      CommonModule,
      FormsModule,
      ReactiveFormsModule,
      HttpClientModule
    ),
    provideZoneChangeDetection({ eventCoalescing: true }),
    {
      provide: TitleStrategy,
      useClass: CustomTitleStrategy
    },

  ]
};
