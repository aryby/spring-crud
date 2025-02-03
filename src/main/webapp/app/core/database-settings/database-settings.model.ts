export class DatabaseSettingsDTO {

  constructor(data:Partial<DatabaseSettingsDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  databaseProvider?: string|null;
  addTimestamps?: boolean|null;

}
