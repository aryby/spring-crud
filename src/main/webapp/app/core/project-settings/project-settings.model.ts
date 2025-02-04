export class ProjectSettingsDTO {

  constructor(data:Partial<ProjectSettingsDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  generalSettings?: number|null;
  databaseSettings?: number|null;
  developerPreferences?: number|null;



}
