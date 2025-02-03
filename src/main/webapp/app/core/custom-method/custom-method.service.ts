import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CustomMethodDTO } from 'app/core/custom-method/custom-method.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class CustomMethodService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/customMethods';

  getAllCustomMethods() {
    return this.http.get<CustomMethodDTO[]>(this.resourcePath);
  }

  getCustomMethod(id: number) {
    return this.http.get<CustomMethodDTO>(this.resourcePath + '/' + id);
  }

  createCustomMethod(customMethodDTO: CustomMethodDTO) {
    return this.http.post<number>(this.resourcePath, customMethodDTO);
  }

  updateCustomMethod(id: number, customMethodDTO: CustomMethodDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, customMethodDTO);
  }

  deleteCustomMethod(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getCustomTableValues() {
    return this.http.get<Record<string,string>>(this.resourcePath + '/customTableValues')
        .pipe(map(transformRecordToMap));
  }

}
