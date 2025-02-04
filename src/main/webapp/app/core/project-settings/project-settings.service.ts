import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { projectSettingDTO } from 'app/core/project-settings/project-settings.model';
import { map } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';


@Injectable({
  providedIn: 'root',
})
export class projectSettingService {

  http = inject(HttpClient);
  resourcePath = environment.apiPath + '/api/projectSettings';

  getAllprojectSettinges() {
    return this.http.get<projectSettingDTO[]>(this.resourcePath);
  }

  getProjectSetting(id: number) {
    return this.http.get<projectSettingDTO>(this.resourcePath + '/' + id);
  }

  getProjectSettingBySlug(slug: string) {
    return this.http.get<projectSettingDTO>(this.resourcePath + '/slug/' + slug);
  }


  download(id: number) {
    return this.http.get(this.resourcePath + '/download/' + id, { responseType:'blob' });
  }

  createProjectSetting(projectSettingDTO: projectSettingDTO) {
    return this.http.post<number>(this.resourcePath, projectSettingDTO);
  }

  updateprojectSetting(id: number, projectSettingDTO: projectSettingDTO) {
    return this.http.put<number>(this.resourcePath + '/' + id, projectSettingDTO);
  }

  deleteprojectSetting(id: number) {
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
