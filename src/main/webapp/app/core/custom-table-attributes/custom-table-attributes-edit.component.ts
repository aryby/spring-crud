import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CustomTableAttributeService } from 'app/core/custom-table-attributes/custom-table-attributes.service';
import { CustomTableAttributeDTO } from 'app/core/custom-table-attributes/custom-table-attributes.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { updateForm } from 'app/common/utils';


@Component({
  selector: 'app-custom-table-attributes-edit',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, InputRowComponent],
  templateUrl: './custom-table-attributes-edit.component.html'
})
export class CustomTableAttributeEditComponent implements OnInit {

  customTableAttributeService = inject(CustomTableAttributeService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    nameTypeModifier: new FormControl(null, [Validators.maxLength(255)]),
    nameAttribute: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    sizeJpaAttributes: new FormControl(null),
    customJoins: new FormControl(null),
    customRelations: new FormControl(null)
  }, { updateOn: 'submit' });

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      updated: $localize`:@@customTableAttribute.update.success:Custom Table Attributes was updated successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.customTableAttributeService.getCustomTableAttribute(this.currentId!)
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
    const data = new CustomTableAttributeDTO(this.editForm.value);
    this.customTableAttributeService.updateCustomTableAttribute(this.currentId!, data)
        .subscribe({
          next: () => this.router.navigate(['/customTableAttribute'], {
            state: {
              msgSuccess: this.getMessage('updated')
            }
          }),
          error: (error) => this.errorHandler.handleServerError(error.error, this.editForm, this.getMessage)
        });
  }

}
