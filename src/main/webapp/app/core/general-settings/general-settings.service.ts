import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { GeneralSettingsDTO } from 'app/core/general-settings/general-settings.model';


@Injectable({
  providedIn: 'root',
})
export class GeneralSettingsService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/generalSettingss';

  getAllGeneralSettingses() {
    return this.http.get<GeneralSettingsDTO[]>(this.resourcePath);
  }

  getGeneralSettings(id: number) {
    return this.http.get<GeneralSettingsDTO>(this.resourcePath + '/' + id);
  }

  createGeneralSettings(generalSettingsDTO: GeneralSettingsDTO) {
    return this.http.post<number>(this.resourcePath, generalSettingsDTO);
  }

  updateGeneralSettings(id: number, generalSettingsDTO: GeneralSettingsDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, generalSettingsDTO);
  }

  deleteGeneralSettings(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
