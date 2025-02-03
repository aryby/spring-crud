import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { DatabaseSettingsService } from 'app/core/database-settings/database-settings.service';
import { DatabaseSettingsDTO } from 'app/core/database-settings/database-settings.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-database-settings-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './database-settings-add.component.html'
})
export class DatabaseSettingsAddComponent {

  databaseSettingsService = inject(DatabaseSettingsService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  addForm = new FormGroup({
    databaseProvider: new FormControl(null, [Validators.maxLength(255)]),
    addTimestamps: new FormControl(false)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@databaseSettings.create.success:Database Settings was created successfully.`
    };
    return messages[key];
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new DatabaseSettingsDTO(this.addForm.value);
    this.databaseSettingsService.createDatabaseSettings(data)
        .subscribe({
          next: () => this.router.navigate(['/databaseSettingss'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
