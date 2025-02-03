import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { GeneralSettingsService } from 'app/core/general-settings/general-settings.service';
import { GeneralSettingsDTO } from 'app/core/general-settings/general-settings.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-general-settings-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './general-settings-edit.component.html'
})
export class GeneralSettingsEditComponent implements OnInit {

  generalSettingsService = inject(GeneralSettingsService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    projectName: new FormControl(null, [Validators.maxLength(255)]),
    buildType: new FormControl(null, [Validators.maxLength(255)]),
    language: new FormControl(null, [Validators.maxLength(255)]),
    enableLombok: new FormControl(false),
    frontendType: new FormControl(null, [Validators.maxLength(255)])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@generalSettings.update.success:General Settings was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.generalSettingsService.getGeneralSettings(this.currentId!)
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
    const data = new GeneralSettingsDTO(this.editForm.value);
    this.generalSettingsService.updateGeneralSettings(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/generalSettingss'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
