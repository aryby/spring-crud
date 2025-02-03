import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { ProjectSettingsService } from 'app/core/project-settings/project-settings.service';
import { ProjectSettingsDTO } from 'app/core/project-settings/project-settings.model';


@Component({
  selector: 'app-project-settings-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './project-settings-list.component.html'})
export class ProjectSettingsListComponent implements OnInit, OnDestroy {

  projectSettingsService = inject(ProjectSettingsService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  projectSettingses?: ProjectSettingsDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@projectSettings.delete.success:Project Settings was removed successfully.`,
      'projectSettings.customTable.projectSettings.referenced': $localize`:@@projectSettings.customTable.projectSettings.referenced:This entity is still referenced by Custom Table ${details?.id} via field Project Settings.`
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
    this.projectSettingsService.getAllProjectSettingses()
        .subscribe({
          next: (data) => this.projectSettingses = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.projectSettingsService.deleteProjectSettings(id)
          .subscribe({
            next: () => this.router.navigate(['/projectSettingss'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/projectSettingss'], {
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

}
