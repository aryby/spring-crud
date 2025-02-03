import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root',
})
export class MyStorageService {

  setEntityToStorage(nameEntity: string, entity: any) {
    localStorage.setItem(nameEntity, JSON.stringify(entity));
  }

  getEntityToStorage(nameEntity: string): any {
    return JSON.parse(<string>localStorage.getItem(nameEntity));
  }

}
