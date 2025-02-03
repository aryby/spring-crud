import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { ProjectSettingsDTO } from 'app/core/project-settings/project-settings.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class ProjectSettingsService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/projectSettingss';

  getAllProjectSettingses() {
    return this.http.get<ProjectSettingsDTO[]>(this.resourcePath);
  }

  getProjectSettings(id: number) {
    return this.http.get<ProjectSettingsDTO>(this.resourcePath + '/' + id);
  }

  createProjectSettings(projectSettingsDTO: ProjectSettingsDTO) {
    return this.http.post<number>(this.resourcePath, projectSettingsDTO);
  }

  updateProjectSettings(id: number, projectSettingsDTO: ProjectSettingsDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, projectSettingsDTO);
  }

  deleteProjectSettings(id: number) {
    return this.http.delete(this.resourcePath + '/' + id);
  }

  getGeneralSettingsValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/generalSettingsValues')
        .pipe(map(transformRecordToMap));
  }

  getDatabaseSettingsValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/databaseSettingsValues')
        .pipe(map(transformRecordToMap));
  }

  getDeveloperPreferencesValues() {
    return this.http.get<Record<string,number>>(this.resourcePath + '/developerPreferencesValues')
        .pipe(map(transformRecordToMap));
  }

}
