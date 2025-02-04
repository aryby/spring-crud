import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import {ReactiveFormsModule, FormControl, FormGroup, Validators, FormsModule} from '@angular/forms';
import {CustomTableService} from 'app/core/custom-table/custom-table.service';
import {CustomTableDTO} from 'app/core/custom-table/custom-table.model';
import {ErrorHandler} from 'app/common/error-handler.injectable';
import {MyStorageService} from "../../services/my-storage";
import {environment} from "../../../environments/environment";
import {DeveloperPreferencesDTO} from "../developer-preferences/developer-preferences.model";
import {projectSettingDTO} from "../project-settings/project-settings.model";
import {CustomTableAttributesDTO} from "../custom-table-attributes/custom-table-attributes.model";


@Component({
  selector: 'app-custom-table-add',
  imports: [CommonModule, RouterLink, ReactiveFormsModule, FormsModule],
  templateUrl: './custom-table-add.component.html'
})
export class CustomTableAddComponent implements OnInit {

  customTableService = inject(CustomTableService);
  _localStore = inject(MyStorageService)
  router = inject(Router);
  errorHandler = inject(ErrorHandler);

  costumTableDTO: CustomTableDTO ={};
  projectSetting: projectSettingDTO ={};
  developerPreferencesTableDTO: DeveloperPreferencesDTO ={};




  addForm = new FormGroup({
    name: new FormControl(null, [Validators.required, Validators.maxLength(255)]),
    customTableAttributes: new FormControl(null),
    projectSetting: new FormControl(null)
  }, {updateOn: 'submit'});
  customTableAttributes: CustomTableAttributesDTO[]=[];

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      created: $localize`:@@customTable.create.success:Custom Table was created successfully.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.developerPreferencesTableDTO = this._localStore.getEntityToStorage(environment.entityPreference);

    if (this._localStore.getEntityToStorage(environment.entityTable)){
      this.costumTableDTO = this._localStore.getEntityToStorage(environment.entityTable);
    }
    this.projectSetting=this._localStore.getEntityToStorage(environment.entityProjectSetting);
    console.log(this.developerPreferencesTableDTO);
    console.log(this.costumTableDTO);

    if (this.costumTableDTO.id != null) {
      this.onSelectTable(this.costumTableDTO.id);
      this.customTableService.customTableAttributes$
        .subscribe({
          next: (data) => this.customTableAttributes = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
    }


  }
  // Load attributes when a table is selected
  onSelectTable(tableId: number) {
    this.customTableService.loadCustomTableAttributesByTableId(tableId);
  }

  handleSubmit() {
    window.scrollTo(0, 0);
    this.addForm.markAllAsTouched();
    this.costumTableDTO.projectSetting = this.projectSetting.id;
    this.customTableService.createCustomTable(this.costumTableDTO)
      .subscribe((value:number) => {
          this.costumTableDTO.id=value;
          this._localStore.setEntityToStorage(environment.entityTable, this.costumTableDTO);
          this.router.navigateByUrl('/customTables/edit/'+this.costumTableDTO.id);
        },
        (error) => this.errorHandler.handleServerError(error.error, this.addForm, this.getMessage)
      );
  }

}
