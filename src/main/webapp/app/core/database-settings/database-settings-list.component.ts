import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { DatabaseSettingsService } from 'app/core/database-settings/database-settings.service';
import { DatabaseSettingsDTO } from 'app/core/database-settings/database-settings.model';


@Component({
  selector: 'app-database-settings-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './database-settings-list.component.html'})
export class DatabaseSettingsListComponent implements OnInit, OnDestroy {

  databaseSettingsService = inject(DatabaseSettingsService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  databaseSettingses?: DatabaseSettingsDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@databaseSettings.delete.success:Database Settings was removed successfully.`,
      'databaseSettings.projectSettings.databaseSettings.referenced': $localize`:@@databaseSettings.projectSettings.databaseSettings.referenced:This entity is still referenced by Project Settings ${details?.id} via field Database Settings.`
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
    this.databaseSettingsService.getAllDatabaseSettingses()
        .subscribe({
          next: (data) => this.databaseSettingses = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.databaseSettingsService.deleteDatabaseSettings(id)
          .subscribe({
            next: () => this.router.navigate(['/databaseSettingss'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/databaseSettingss'], {
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
