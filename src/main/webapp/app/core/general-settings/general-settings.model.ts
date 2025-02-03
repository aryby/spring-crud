export class GeneralSettingsDTO {

  constructor(data:Partial<GeneralSettingsDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  projectName?: string|null;
  buildType?: string|null;
  language?: string|null;
  enableLombok?: boolean|null;
  frontendType?: string|null;

}
