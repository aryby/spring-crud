import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { projectSettingService } from 'app/core/project-settings/project-settings.service';
import { projectSettingDTO } from 'app/core/project-settings/project-settings.model';
import {MyStorageService} from "../../services/my-storage";
import {environment} from "../../../environments/environment";


@Component({
  selector: 'app-project-settings-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './project-settings-list.component.html'})
export class projectSettingListComponent implements OnInit, OnDestroy {

  projectSettingService = inject(projectSettingService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  projectSettinges?: projectSettingDTO[];
  navigationSubscription?: Subscription;

  _myStorage = inject(MyStorageService);

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@projectSetting.delete.success:Project Settings was removed successfully.`,
      'projectSetting.customTable.projectSetting.referenced': $localize`:@@projectSetting.customTable.projectSetting.referenced:This entity is still referenced by Custom Table ${details?.id} via field Project Settings.`
    };
    return messages[key];
  }

  ngOnInit() {
    this.loadData();
    this.navigationSubscription = this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.loadData();
      }
    });
  }

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }

  loadData() {
    this.projectSettingService.getAllprojectSettinges()
        .subscribe({
          next: (data) => this.projectSettinges = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.projectSettingService.deleteprojectSetting(id)
          .subscribe({
            next: () => this.router.navigate(['/projectSettings'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/projectSettings'], {
                  state: {
                    msgError: this.getMessage(messageParts[0], { id: messageParts[1] })
                  }
                });
                return;
              }
              this.errorHandler.handleServerError(error.error)
            }
          });
    }
  }


  downloadZip() {
    let cusT = this._myStorage.getEntityToStorage(environment.entityProjectSetting);
    this.projectSettingService.download(cusT.id)
      .subscribe(blob => {
        const a = document.createElement('a');
        const objectUrl = URL.createObjectURL(blob);
        a.href = objectUrl;
        a.download = 'generated_project.zip';
        a.click();
        URL.revokeObjectURL(objectUrl);
      });
  }
}
