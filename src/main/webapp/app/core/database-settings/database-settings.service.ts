import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { DatabaseSettingsDTO } from 'app/core/database-settings/database-settings.model';


@Injectable({
  providedIn: 'root',
})
export class DatabaseSettingsService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/databaseSettingss';

  getAllDatabaseSettingses() {
    return this.http.get<DatabaseSettingsDTO[]>(this.resourcePath);
  }

  getDatabaseSettings(id: number) {
    return this.http.get<DatabaseSettingsDTO>(this.resourcePath + '/' + id);
  }

  createDatabaseSettings(databaseSettingsDTO: DatabaseSettingsDTO) {
    return this.http.post<number>(this.resourcePath, databaseSettingsDTO);
  }

  updateDatabaseSettings(id: number, databaseSettingsDTO: DatabaseSettingsDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, databaseSettingsDTO);
  }

  deleteDatabaseSettings(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

}
