import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CustomTableAttributeDTO } from 'app/core/custom-table-attributes/custom-table-attributes.model';


@Injectable({
  providedIn: 'root',
})
export class CustomTableAttributeService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/customTableAttribute';

  getAllCustomTableAttributees() {
    return this.http.get<CustomTableAttributeDTO[]>(this.resourcePath);
  }

  getCustomTableAttribute(id: number) {
    return this.http.get<CustomTableAttributeDTO>(this.resourcePath + '/' + id);
  }

  createCustomTableAttribute(customTableAttributeDTO: CustomTableAttributeDTO) {
    return this.http.post<number>(this.resourcePath, customTableAttributeDTO);
  }

  updateCustomTableAttribute(id: number, customTableAttributeDTO: CustomTableAttributeDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, customTableAttributeDTO);
  }

  deleteCustomTableAttribute(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
