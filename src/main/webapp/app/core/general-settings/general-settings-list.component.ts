import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { GeneralSettingsService } from 'app/core/general-settings/general-settings.service';
import { GeneralSettingsDTO } from 'app/core/general-settings/general-settings.model';


@Component({
  selector: 'app-general-settings-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './general-settings-list.component.html'})
export class GeneralSettingsListComponent implements OnInit, OnDestroy {

  generalSettingsService = inject(GeneralSettingsService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  generalSettingses?: GeneralSettingsDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@generalSettings.delete.success:General Settings was removed successfully.`,
      'generalSettings.projectSettings.generalSettings.referenced': $localize`:@@generalSettings.projectSettings.generalSettings.referenced:This entity is still referenced by Project Settings ${details?.id} via field General Settings.`
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
    this.generalSettingsService.getAllGeneralSettingses()
        .subscribe({
          next: (data) => this.generalSettingses = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.generalSettingsService.deleteGeneralSettings(id)
          .subscribe({
            next: () => this.router.navigate(['/generalSettingss'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/generalSettingss'], {
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
