import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import {DeveloperPreferencesDTO} from "./developer-preferences.model";


@Injectable({
  providedIn: 'root',
})
export class DeveloperPreferencesService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/developerPreferencess';

  getAllDeveloperPreferenceses() {
    return this.http.get<DeveloperPreferencesDTO[]>(this.resourcePath);
  }

  getDeveloperPreferences(id: number) {
    return this.http.get<DeveloperPreferencesDTO>(this.resourcePath + '/' + id);
  }

  createDeveloperPreferences(developerPreferencesDTO: DeveloperPreferencesDTO) {
    return this.http.post<number>(this.resourcePath, developerPreferencesDTO);
  }

  updateDeveloperPreferences(id: number, developerPreferencesDTO: DeveloperPreferencesDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, developerPreferencesDTO);
  }

  deleteDeveloperPreferences(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
