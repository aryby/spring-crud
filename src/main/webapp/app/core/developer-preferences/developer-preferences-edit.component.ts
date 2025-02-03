import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';
import {DeveloperPreferencesService} from "./developer-preferences.service";
import {DeveloperPreferencesDTO} from "./developer-preferences.model";


@Component({
  selector: 'app-developer-preferences-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './developer-preferences-edit.component.html'
})
export class DeveloperPreferencesEditComponent implements OnInit {

  developerPreferencesService = inject(DeveloperPreferencesService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    appFormat: new FormControl(null, [Validators.maxLength(255)]),
    packageStrategy: new FormControl(null, [Validators.maxLength(255)]),
    enableOpenAPI: new FormControl(false),
    useDockerCompose: new FormControl(false),
    javaVersion: new FormControl(null, [Validators.maxLength(255)]),
    furtherDependencies: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@developerPreferences.update.success:Developer Preferences was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.developerPreferencesService.getDeveloperPreferences(this.currentId!)
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
    const data = new DeveloperPreferencesDTO(this.editForm.value);
    this.developerPreferencesService.updateDeveloperPreferences(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/developerPreferencess'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
