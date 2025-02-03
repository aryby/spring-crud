import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CustomMethodService } from 'app/core/custom-method/custom-method.service';
import { CustomMethodDTO } from 'app/core/custom-method/custom-method.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-custom-method-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './custom-method-edit.component.html'
})
export class CustomMethodEditComponent implements OnInit {

  customMethodService = inject(CustomMethodService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  customTableValues?: Map<number,string>;
  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    methodName: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    methodBody: new FormControl(null),
    annotations: new FormControl(null),
    customTable: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@customMethod.update.success:Custom Method was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.customMethodService.getCustomTableValues()
        .subscribe({
          next: (data) => this.customTableValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    this.customMethodService.getCustomMethod(this.currentId!)
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
    const data = new CustomMethodDTO(this.editForm.value);
    this.customMethodService.updateCustomMethod(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/customMethods'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
