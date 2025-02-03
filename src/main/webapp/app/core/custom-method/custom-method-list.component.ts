import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { CustomMethodService } from 'app/core/custom-method/custom-method.service';
import { CustomMethodDTO } from 'app/core/custom-method/custom-method.model';


@Component({
  selector: 'app-custom-method-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './custom-method-list.component.html'})
export class CustomMethodListComponent implements OnInit, OnDestroy {

  customMethodService = inject(CustomMethodService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  customMethods?: CustomMethodDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@customMethod.delete.success:Custom Method was removed successfully.`    };
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
    this.customMethodService.getAllCustomMethods()
        .subscribe({
          next: (data) => this.customMethods = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.customMethodService.deleteCustomMethod(id)
          .subscribe({
            next: () => this.router.navigate(['/customMethods'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => this.errorHandler.handleServerError(error.error)
          });
    }
  }

}
