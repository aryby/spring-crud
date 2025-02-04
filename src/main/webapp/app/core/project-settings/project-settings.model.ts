export class projectSettingDTO {

  constructor(data:Partial<projectSettingDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  generalSettings?: number|null;
  databaseSettings?: number|null;
  developerPreferences?: number|null;



}
