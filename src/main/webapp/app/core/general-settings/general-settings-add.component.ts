import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { GeneralSettingsService } from 'app/core/general-settings/general-settings.service';
import { GeneralSettingsDTO } from 'app/core/general-settings/general-settings.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-general-settings-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './general-settings-add.component.html'
})
export class GeneralSettingsAddComponent {

  generalSettingsService = inject(GeneralSettingsService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    projectName: new FormControl(null, [Validators.maxLength(255)]),
    buildType: new FormControl(null, [Validators.maxLength(255)]),
    language: new FormControl(null, [Validators.maxLength(255)]),
    enableLombok: new FormControl(false),
    frontendType: new FormControl(null, [Validators.maxLength(255)])
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@generalSettings.create.success:General Settings was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new GeneralSettingsDTO(this.addForm.value);
    this.generalSettingsService.createGeneralSettings(data)
        .subscribe({
          next: () => this.router.navigate(['/generalSettingss'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
