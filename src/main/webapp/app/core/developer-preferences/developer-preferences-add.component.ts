import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import {DeveloperPreferencesService} from "./developer-preferences.service";
import {DeveloperPreferencesDTO} from "./developer-preferences.model";


@Component({
  selector: 'app-developer-preferences-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './developer-preferences-add.component.html'
})
export class DeveloperPreferencesAddComponent {

  developerPreferencesService = inject(DeveloperPreferencesService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    appFormat: new FormControl(null, [Validators.maxLength(255)]),
    packageStrategy: new FormControl(null, [Validators.maxLength(255)]),
    enableOpenAPI: new FormControl(false),
    useDockerCompose: new FormControl(false),
    javaVersion: new FormControl(null, [Validators.maxLength(255)]),
    furtherDependencies: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@developerPreferences.create.success:Developer Preferences was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new DeveloperPreferencesDTO(this.addForm.value);
    this.developerPreferencesService.createDeveloperPreferences(data)
        .subscribe({
          next: () => this.router.navigate(['/developerPreferencess'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
