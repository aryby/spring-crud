export class CustomTableAttributeDTO {

  constructor(data:Partial<CustomTableAttributeDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  nameTypeModifier?: string|null;
  nameAttribute?: string|null;
  customTable?:number|null;
  sizeJpaAttributes?: string|null;
  customJoins?: string|null;
  customRelations?: string|null;

}
