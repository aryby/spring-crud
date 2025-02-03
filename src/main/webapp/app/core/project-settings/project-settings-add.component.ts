import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ProjectSettingsService } from 'app/core/project-settings/project-settings.service';
import { ProjectSettingsDTO } from 'app/core/project-settings/project-settings.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-project-settings-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './project-settings-add.component.html'
})
export class ProjectSettingsAddComponent implements OnInit {

  projectSettingsService = inject(ProjectSettingsService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  generalSettingsValues?: Map<number,string>;
  databaseSettingsValues?: Map<number,string>;
  developerPreferencesValues?: Map<number,string>;

  addForm = new FormGroup({
    generalSettings: new FormControl(null),
    databaseSettings: new FormControl(null),
    developerPreferences: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@projectSettings.create.success:Project Settings was created successfully.`,
      PROJECT_SETTINGS_GENERAL_SETTINGS_UNIQUE: $localize`:@@Exists.projectSettings.generalSettings:This General Settings is already referenced by another Project Settings.`,
      PROJECT_SETTINGS_DATABASE_SETTINGS_UNIQUE: $localize`:@@Exists.projectSettings.databaseSettings:This Database Settings is already referenced by another Project Settings.`,
      PROJECT_SETTINGS_DEVELOPER_PREFERENCES_UNIQUE: $localize`:@@Exists.projectSettings.developerPreferences:This Developer Preferences is already referenced by another Project Settings.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.projectSettingsService.getGeneralSettingsValues()
        .subscribe({
          next: (data) => this.generalSettingsValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.projectSettingsService.getDatabaseSettingsValues()
        .subscribe({
          next: (data) => this.databaseSettingsValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.projectSettingsService.getDeveloperPreferencesValues()
        .subscribe({
          next: (data) => this.developerPreferencesValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new ProjectSettingsDTO(this.addForm.value);
    this.projectSettingsService.createProjectSettings(data)
        .subscribe({
          next: () => this.router.navigate(['/projectSettingss'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
