import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { projectSettingService } from 'app/core/project-settings/project-settings.service';
import { projectSettingDTO } from 'app/core/project-settings/project-settings.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-project-settings-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './project-settings-edit.component.html'
})
export class projectSettingEditComponent implements OnInit {

  projectSettingService = inject(projectSettingService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  generalSettingsValues?: Map<number,string>;
  databaseSettingsValues?: Map<number,string>;
  developerPreferencesValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    generalSettings: new FormControl(null),
    databaseSettings: new FormControl(null),
    developerPreferences: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@projectSetting.update.success:Project Settings was updated successfully.`,
      PROJECT_SETTINGS_GENERAL_SETTINGS_UNIQUE: $localize`:@@Exists.projectSetting.generalSettings:This General Settings is already referenced by another Project Settings.`,
      PROJECT_SETTINGS_DATABASE_SETTINGS_UNIQUE: $localize`:@@Exists.projectSetting.databaseSettings:This Database Settings is already referenced by another Project Settings.`,
      PROJECT_SETTINGS_DEVELOPER_PREFERENCES_UNIQUE: $localize`:@@Exists.projectSetting.developerPreferences:This Developer Preferences is already referenced by another Project Settings.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.projectSettingService.getGeneralSettingsValues()
        .subscribe({
          next: (data) => this.generalSettingsValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.projectSettingService.getDatabaseSettingsValues()
        .subscribe({
          next: (data) => this.databaseSettingsValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.projectSettingService.getDeveloperPreferencesValues()
        .subscribe({
          next: (data) => this.developerPreferencesValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.projectSettingService.getprojectSetting(this.currentId!)
        .subscribe({
          next: (data) => updateForm(this.editForm, data),
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.editForm.markAllAsTouched();
    if (!this.editForm.valid) {
      return;
    }
    const data = new projectSettingDTO(this.editForm.value);
    this.projectSettingService.updateprojectSetting(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/projectSettings'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
