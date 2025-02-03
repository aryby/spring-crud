import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { CustomTableAttributesService } from 'app/core/custom-table-attributes/custom-table-attributes.service';
import { CustomTableAttributesDTO } from 'app/core/custom-table-attributes/custom-table-attributes.model';


@Component({
  selector: 'app-custom-table-attributes-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './custom-table-attributes-list.component.html'})
export class CustomTableAttributesListComponent implements OnInit, OnDestroy {

  customTableAttributesService = inject(CustomTableAttributesService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  customTableAttributeses?: CustomTableAttributesDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@customTableAttributes.delete.success:Custom Table Attributes was removed successfully.`,
      'customTableAttributes.customTable.customTableAttributes.referenced': $localize`:@@customTableAttributes.customTable.customTableAttributes.referenced:This entity is still referenced by Custom Table ${details?.id} via field Custom Table Attributes.`
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
    this.customTableAttributesService.getAllCustomTableAttributeses()
        .subscribe({
          next: (data) => this.customTableAttributeses = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.customTableAttributesService.deleteCustomTableAttributes(id)
          .subscribe({
            next: () => this.router.navigate(['/customTableAttributess'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/customTableAttributess'], {
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
