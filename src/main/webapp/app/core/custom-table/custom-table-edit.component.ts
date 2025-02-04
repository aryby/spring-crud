import { Component, inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import { InputRowComponent } from 'app/common/input-row/input-row.component';
import { CustomTableService } from 'app/core/custom-table/custom-table.service';
import { CustomTableDTO } from 'app/core/custom-table/custom-table.model';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import {CustomTableAttributesAddComponent} from "../custom-table-attributes/custom-table-attributes-add.component";
import {CustomTableAttributesDTO} from "../custom-table-attributes/custom-table-attributes.model";


@Component({
  selector: 'app-custom-table-edit',
  imports: [CommonModule, ReactiveFormsModule, CustomTableAttributesAddComponent],
  templateUrl: './custom-table-edit.component.html'
})
export class CustomTableEditComponent implements OnInit {

  customTableService = inject(CustomTableService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  currentId?: number;

  editForm = new FormGroup({
    id: new FormControl({ value: null, disabled: true }),
    name: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    customTableAttributes: new FormControl(null),
    projectSettings: new FormControl(null)
  }, { updateOn: 'submit' });
  customTableDTO: CustomTableDTO={};
  customTableAttributes: CustomTableAttributesDTO[]=[];






  // Load attributes when a table is selected
  onSelectTable(tableId: number) {
    this.customTableService.loadCustomTableAttributesByTableId(tableId);
  }

  ngOnInit() {
    this.currentId = +this.route.snapshot.params['id'];
    this.onSelectTable(this.currentId);
    this.customTableService.getCustomTable(this.currentId!)
        .subscribe({
          next: (data) => {
            this.customTableDTO = data;

          },
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }


}
