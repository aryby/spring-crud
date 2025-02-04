import {Component, inject} from '@angular/core';
import { environment } from 'environments/environment';
import {projectSettingDTO} from "../project-settings/project-settings.model";
import {DatabaseSettingsDTO} from "../database-settings/database-settings.model";
import {GeneralSettingsDTO} from "../general-settings/general-settings.model";
import {DeveloperPreferencesDTO} from "../developer-preferences/developer-preferences.model";
import {FormsModule} from "@angular/forms";
import {GeneralSettingsService} from "../general-settings/general-settings.service";
import {DeveloperPreferencesService} from "../developer-preferences/developer-preferences.service";
import {DatabaseSettingsService} from "../database-settings/database-settings.service";
import {projectSettingService} from "../project-settings/project-settings.service";
import {MyStorageService} from "../../services/my-storage";
import {Router} from "@angular/router";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',

  imports: [
    FormsModule
  ]
})
export class HomeComponent {

  environment = environment;

  _storage=inject(MyStorageService);
  router:Router=inject(Router);
  generalSettingsService= inject(GeneralSettingsService);
  developerPreferencesService= inject(DeveloperPreferencesService);
  databaseSettingsService= inject(DatabaseSettingsService);
  projectSettingService= inject(projectSettingService);

  generalSettingsDTO :GeneralSettingsDTO={
    buildType:'maven',
    enableLombok:true,
    frontendType:'',
    language:'java',
    projectName:'com.example.name',
  }
  developerPreferencesDto:DeveloperPreferencesDTO={
    appFormat:'Properties Or Yaml',
    enableOpenAPI:true,
    furtherDependencies:'',
    javaVersion:'17.0',
    packageStrategy:'',
    useDockerCompose:false,
  }
  databaseSettingDto: DatabaseSettingsDTO = {
    databaseProvider:'',
    addTimestamps:true,
  };
  projectSettingDto:projectSettingDTO={

  };
    i:number = 0;
    test = ()=> {
      let vael=this.i >= 3;
      if (vael){
        this.projectSettingService.createprojectSetting(this.projectSettingDto).subscribe(value => {
          this.projectSettingDto.id=value;
          this._storage.setEntityToStorage(environment.entityProjectSetting, this.projectSettingDto);
          this.router.navigateByUrl('/customTables');
        })
        this.i=0;
        return true;
      }
      return false;

    };
  sendProject() {

    this.generalSettingsService.createGeneralSettings(this.generalSettingsDTO).subscribe(val=>{
      this.generalSettingsDTO.id=val;
      this._storage.setEntityToStorage(environment.entityGeneralSetting, this.generalSettingsDTO);

      this.projectSettingDto.generalSettings=val;
      this.i++;
      console.log("General Settings DTO i : "+this.i);
      console.log("General Settings DTO test : "+this.test());
    });
    this.developerPreferencesService.createDeveloperPreferences(this.developerPreferencesDto).subscribe(val=>{
      this.developerPreferencesDto.id=val;
      this._storage.setEntityToStorage(environment.entityPreference, this.developerPreferencesDto);

      this.projectSettingDto.developerPreferences=val;
      this.i++;
      console.log("Developer Preferences DTO i : "+this.i);
      console.log("Developer Preferences DTO test : "+this.test());
    });
    this.databaseSettingsService.createDatabaseSettings(this.databaseSettingDto).subscribe(val=>{
      this.databaseSettingDto.id=val;
      this._storage.setEntityToStorage(environment.entityDatabaseSettings, this.databaseSettingDto);

      this.projectSettingDto.databaseSettings=val;
      this.i++;
      console.log("Database Settings DTO i : "+this.i);
      console.log("Database Settings DTO test : "+this.test());
    })

    console.log("this.databaseSettingDto")
    console.log(this.databaseSettingDto)
    console.log("this.developerPreferencesDto")
    console.log(this.developerPreferencesDto)
    console.log("this.generalSettingsDTO")
    console.log(this.generalSettingsDTO)
  }
}
