import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CustomTableDTO } from 'app/core/custom-table/custom-table.model';
import {map, Observable} from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';
import {CustomTableAttributesDTO} from "../custom-table-attributes/custom-table-attributes.model";


@Injectable({
  providedIn: 'root',
})
export class CustomTableService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/customTables';

  getAllCustomTables() {
    return this.http.get<CustomTableDTO[]>(this.resourcePath);
  }

  getCustomTableAttributesByTableId(id:number) {
    return this.http.get<CustomTableAttributesDTO[]>(this.resourcePath + '/customTable/'+id);
  }

  getCustomTable(id: number) {
    return this.http.get<CustomTableDTO>(this.resourcePath + '/' + id);
  }

  createCustomTable(customTableDTO: CustomTableDTO):Observable<number> {
    return this.http.post<number>(this.resourcePath, customTableDTO);
  }

  updateCustomTable(id: number, customTableDTO: CustomTableDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, customTableDTO);
  }

  deleteCustomTable(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }



  getProjectSettingsValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/projectSettingsValues')
        .pipe(map(transformRecordToMap));
  }

}
