import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CustomTableAttributesDTO } from 'app/core/custom-table-attributes/custom-table-attributes.model';


@Injectable({
  providedIn: 'root',
})
export class CustomTableAttributesService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/customTableAttributess';

  getAllCustomTableAttributeses() {
    return this.http.get<CustomTableAttributesDTO[]>(this.resourcePath);
  }

  getCustomTableAttributes(id: number) {
    return this.http.get<CustomTableAttributesDTO>(this.resourcePath + '/' + id);
  }

  createCustomTableAttributes(customTableAttributesDTO: CustomTableAttributesDTO) {
    return this.http.post<number>(this.resourcePath, customTableAttributesDTO);
  }

  updateCustomTableAttributes(id: number, customTableAttributesDTO: CustomTableAttributesDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, customTableAttributesDTO);
  }

  deleteCustomTableAttributes(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
