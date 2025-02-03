import { Routes } from '@angular/router';
import { HomeComponent } from './core/home/home.component';
import { CustomTableListComponent } from './core/custom-table/custom-table-list.component';
import { CustomTableAddComponent } from './core/custom-table/custom-table-add.component';
import { CustomTableEditComponent } from './core/custom-table/custom-table-edit.component';
import { CustomTableAttributesListComponent } from './core/custom-table-attributes/custom-table-attributes-list.component';
import { CustomTableAttributesAddComponent } from './core/custom-table-attributes/custom-table-attributes-add.component';
import { CustomTableAttributesEditComponent } from './core/custom-table-attributes/custom-table-attributes-edit.component';
import { CustomMethodListComponent } from './core/custom-method/custom-method-list.component';
import { CustomMethodAddComponent } from './core/custom-method/custom-method-add.component';
import { CustomMethodEditComponent } from './core/custom-method/custom-method-edit.component';
import { ProjectSettingsListComponent } from './core/project-settings/project-settings-list.component';
import { ProjectSettingsAddComponent } from './core/project-settings/project-settings-add.component';
import { ProjectSettingsEditComponent } from './core/project-settings/project-settings-edit.component';
import { GeneralSettingsListComponent } from './core/general-settings/general-settings-list.component';
import { GeneralSettingsAddComponent } from './core/general-settings/general-settings-add.component';
import { GeneralSettingsEditComponent } from './core/general-settings/general-settings-edit.component';
import { DatabaseSettingsListComponent } from './core/database-settings/database-settings-list.component';
import { DatabaseSettingsAddComponent } from './core/database-settings/database-settings-add.component';
import { DatabaseSettingsEditComponent } from './core/database-settings/database-settings-edit.component';
import { ErrorComponent } from './core/error/error.component';
import {DeveloperPreferencesListComponent} from "./core/developer-preferences/developer-preferences-list.component";
import {DeveloperPreferencesAddComponent} from "./core/developer-preferences/developer-preferences-add.component";
import {DeveloperPreferencesEditComponent} from "./core/developer-preferences/developer-preferences-edit.component";


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`
  },
  {
    path: 'customTables',
    component: CustomTableListComponent,
    title: $localize`:@@customTable.list.headline:Custom Tables`
  },
  {
    path: 'customTables/add',
    component: CustomTableAddComponent,
    title: $localize`:@@customTable.add.headline:Add Custom Table`
  },
  {
    path: 'customTables/edit/:id',
    component: CustomTableEditComponent,
    title: $localize`:@@customTable.edit.headline:Edit Custom Table`
  },
  {
    path: 'customTableAttributess',
    component: CustomTableAttributesListComponent,
    title: $localize`:@@customTableAttributes.list.headline:Custom Table Attributeses`
  },
  {
    path: 'customTableAttributess/add',
    component: CustomTableAttributesAddComponent,
    title: $localize`:@@customTableAttributes.add.headline:Add Custom Table Attributes`
  },
  {
    path: 'customTableAttributess/edit/:id',
    component: CustomTableAttributesEditComponent,
    title: $localize`:@@customTableAttributes.edit.headline:Edit Custom Table Attributes`
  },
  {
    path: 'customMethods',
    component: CustomMethodListComponent,
    title: $localize`:@@customMethod.list.headline:Custom Methods`
  },
  {
    path: 'customMethods/add',
    component: CustomMethodAddComponent,
    title: $localize`:@@customMethod.add.headline:Add Custom Method`
  },
  {
    path: 'customMethods/edit/:id',
    component: CustomMethodEditComponent,
    title: $localize`:@@customMethod.edit.headline:Edit Custom Method`
  },
  {
    path: 'projectSettingss',
    component: ProjectSettingsListComponent,
    title: $localize`:@@projectSettings.list.headline:Project Settingses`
  },
  {
    path: 'projectSettingss/add',
    component: ProjectSettingsAddComponent,
    title: $localize`:@@projectSettings.add.headline:Add Project Settings`
  },
  {
    path: 'projectSettingss/edit/:id',
    component: ProjectSettingsEditComponent,
    title: $localize`:@@projectSettings.edit.headline:Edit Project Settings`
  },
  {
    path: 'generalSettingss',
    component: GeneralSettingsListComponent,
    title: $localize`:@@generalSettings.list.headline:General Settingses`
  },
  {
    path: 'generalSettingss/add',
    component: GeneralSettingsAddComponent,
    title: $localize`:@@generalSettings.add.headline:Add General Settings`
  },
  {
    path: 'generalSettingss/edit/:id',
    component: GeneralSettingsEditComponent,
    title: $localize`:@@generalSettings.edit.headline:Edit General Settings`
  },
  {
    path: 'developerPreferencess',
  component: DeveloperPreferencesListComponent,
  title: $localize`:@@developerPreferencess.list.headline:developer Pereferences`
},{
    path: 'developerPreferencess/add',
    component: DeveloperPreferencesAddComponent,
    title: $localize`:@@developerPreferencess.add.headline:add developer Pereferences`
  },{
    path: 'developerPreferencess/edit/:id',
    component: DeveloperPreferencesEditComponent,
    title: $localize`:@@developerPreferencess.edit.headline:edit developer Pereferences`
  },
  {
    path: 'databaseSettingss',
    component: DatabaseSettingsListComponent,
    title: $localize`:@@databaseSettings.list.headline:Database Settingses`
  },
  {
    path: 'databaseSettingss/add',
    component: DatabaseSettingsAddComponent,
    title: $localize`:@@databaseSettings.add.headline:Add Database Settings`
  },
  {
    path: 'databaseSettingss/edit/:id',
    component: DatabaseSettingsEditComponent,
    title: $localize`:@@databaseSettings.edit.headline:Edit Database Settings`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];
