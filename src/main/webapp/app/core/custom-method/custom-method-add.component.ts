import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CustomMethodService } from 'app/core/custom-method/custom-method.service';
import { CustomMethodDTO } from 'app/core/custom-method/custom-method.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';


@Component({
  selector: 'app-custom-method-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './custom-method-add.component.html'
})
export class CustomMethodAddComponent implements OnInit {

  customMethodService = inject(CustomMethodService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  customTableValues?: Map<number,string>;

  addForm = new FormGroup({
    methodName: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    methodBody: new FormControl(null),
    annotations: new FormControl(null),
    customTable: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@customMethod.create.success:Custom Method was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.customMethodService.getCustomTableValues()
        .subscribe({
          next: (data) => this.customTableValues = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    if (!this.addForm.valid) {
      return;
    }
    const data = new CustomMethodDTO(this.addForm.value);
    this.customMethodService.createCustomMethod(data)
        .subscribe({
          next: () => this.router.navigate(['/customMethods'], {
            state: {
              msgSuccess: this.getMessage('created')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
        });
  }

}
