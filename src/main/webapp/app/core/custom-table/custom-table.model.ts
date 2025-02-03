export class CustomTableDTO {

  constructor(data:Partial<CustomTableDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  name?: string|null;
  customTablesAttributes?: number[]|null;
  projectSettings?: number|null;

}
