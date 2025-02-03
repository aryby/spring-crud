import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { DatabaseSettingsService } from 'app/core/database-settings/database-settings.service';
import { DatabaseSettingsDTO } from 'app/core/database-settings/database-settings.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-database-settings-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './database-settings-edit.component.html'
})
export class DatabaseSettingsEditComponent implements OnInit {

  databaseSettingsService = inject(DatabaseSettingsService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    databaseProvider: new FormControl(null, [Validators.maxLength(255)]),
    addTimestamps: new FormControl(false)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@databaseSettings.update.success:Database Settings was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.databaseSettingsService.getDatabaseSettings(this.currentId!)
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
    const data = new DatabaseSettingsDTO(this.editForm.value);
    this.databaseSettingsService.updateDatabaseSettings(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/databaseSettingss'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
