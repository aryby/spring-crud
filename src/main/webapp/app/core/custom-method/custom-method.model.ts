export class CustomMethodDTO {

  constructor(data:Partial<CustomMethodDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  methodName?: string|null;
  methodBody?: string|null;
  annotations?: string|null;
  customTable?: number|null;

}
