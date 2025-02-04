import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router} from '@angular/router';
import {ReactiveFormsModule, FormControl, FormGroup, Validators, FormsModule} from '@angular/forms';
import {CustomTableAttributesService} from 'app/core/custom-table-attributes/custom-table-attributes.service';
import {CustomTableAttributesDTO} from 'app/core/custom-table-attributes/custom-table-attributes.model';
import {ErrorHandler} from 'app/common/error-handler.injectable';
import {MyStorageService} from "../../services/my-storage";
import {environment} from "../../../environments/environment";
import {CustomTableDTO} from "../custom-table/custom-table.model";
import {CustomTableService} from "../custom-table/custom-table.service";


@Component({
  selector: 'app-custom-table-attributes-add',
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './custom-table-attributes-add.component.html'
})
export class CustomTableAttributesAddComponent {

  accoladeOpen='{';
  accoladeClose='}';
  customTableAttributesService = inject(CustomTableAttributesService);
  customTableService = inject(CustomTableService);
  router = inject(Router);
  errorHandler = inject(ErrorHandler);
  nameTypeModifiersValues: Record<string, string> | Map<number, string> | undefined;
  _mystorage = inject(MyStorageService);
  customTableAttributes: CustomTableAttributesDTO[] = []
  customTableAttributesDTO: CustomTableAttributesDTO = {};
  customTableDTO: CustomTableDTO = {id: 0};

  initnameTypeModifiersValues(): void {
    this.nameTypeModifiersValues = new Map<number, string>([
      [1, "String"],
      [2, "boolean"],
      [3, "Long"],
      [4, "int"],
      [5, "List"],
    ]);
  }

  constructor() {
    this.initnameTypeModifiersValues();


    if (this._mystorage.getEntityToStorage(environment.entityTable)) {
      this.customTableDTO = this._mystorage.getEntityToStorage(environment.entityTable);
      this.customTableService.loadCustomTableAttributesByTableId(typeof this.customTableDTO.id === "number" ? this.customTableDTO.id :-1);
      this.customTableService.customTableAttributes$.subscribe(customTableAttributes => {
        this.customTableAttributes = customTableAttributes;
      })
    }
  }

  addForm = new FormGroup({
    nameTypeModifier: new FormControl(null, [Validators.maxLength(255)]),
    nameAttribute: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    sizeJpaAttributes: new FormControl(null),
    customJoins: new FormControl(null),
    customRelations: new FormControl(null)
  }, {updateOn: 'submit'});


  getMessage(key: string) {
    const messages: Record<string, string> = {
      created: $localize`:@@customTableAttributes.create.success:Custom Table Attributes was created successfully.`
    };
    return messages[key];
  }


  handleSubmit() {
    window.scrollTo(0, 0);

    this.customTableAttributesDTO.customTable=this.customTableDTO.id;
    this.customTableAttributesService.createCustomTableAttributes(this.customTableAttributesDTO)
      .subscribe({
        next: (data: number) => {
          this.customTableAttributesDTO.id = data;
          this.customTableDTO.customTablesAttributes?.push(data);
         console.log("attributes added successfully.");
          console.log(this.customTableDTO);
          this.customTableAttributes.push(this.customTableAttributesDTO);
          this._mystorage.setEntityToStorage(environment.customTableAttributes, this.customTableAttributesDTO);
        },
        error: (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
      });
  }

}
