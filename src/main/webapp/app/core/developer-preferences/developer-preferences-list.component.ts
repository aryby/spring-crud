import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import {DeveloperPreferencesService} from "./developer-preferences.service";
import {DeveloperPreferencesDTO} from "./developer-preferences.model";


@Component({
  selector: 'app-developer-preferences-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './developer-preferences-list.component.html'})
export class DeveloperPreferencesListComponent implements OnInit, OnDestroy {

  developerPreferencesService = inject(DeveloperPreferencesService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  developerPreferenceses?: DeveloperPreferencesDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@developerPreferences.delete.success:Developer Preferences was removed successfully.`,
      'developerPreferences.projectSetting.developerPreferences.referenced': $localize`:@@developerPreferences.projectSetting.developerPreferences.referenced:This entity is still referenced by Project Settings ${details?.id} via field Developer Preferences.`
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
    this.developerPreferencesService.getAllDeveloperPreferenceses()
        .subscribe({
          next: (data) => this.developerPreferenceses = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.developerPreferencesService.deleteDeveloperPreferences(id)
          .subscribe({
            next: () => this.router.navigate(['/developerPreferencess'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/developerPreferencess'], {
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
