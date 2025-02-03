export class DeveloperPreferencesDTO {

  constructor(data:Partial<DeveloperPreferencesDTO>) {
    Object.assign(this, data);
  }

  id?: number|null;
  appFormat?: string|null;
  packageStrategy?: string|null;
  enableOpenAPI?: boolean|null;
  useDockerCompose?: boolean|null;
  javaVersion?: string|null;
  furtherDependencies?: string|null;

}
