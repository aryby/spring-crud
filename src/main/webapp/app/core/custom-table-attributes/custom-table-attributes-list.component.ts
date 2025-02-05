import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationEnd, Router, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { ErrorHandler } from 'app/common/error-handler.injectable';
import { CustomTableAttributeService } from 'app/core/custom-table-attributes/custom-table-attributes.service';
import { CustomTableAttributeDTO } from 'app/core/custom-table-attributes/custom-table-attributes.model';


@Component({
  selector: 'app-custom-table-attributes-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './custom-table-attributes-list.component.html'})
export class CustomTableAttributeListComponent implements OnInit, OnDestroy {

  customTableAttributeService = inject(CustomTableAttributeService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  customTableAttributees?: CustomTableAttributeDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@customTableAttribute.delete.success:Custom Table Attributes was removed successfully.`,
      'customTableAttribute.customTable.customTableAttribute.referenced': $localize`:@@customTableAttribute.customTable.customTableAttribute.referenced:This entity is still referenced by Custom Table ${details?.id} via field Custom Table Attributes.`
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
    this.customTableAttributeService.getAllCustomTableAttributees()
        .subscribe({
          next: (data) => this.customTableAttributees = data,
          error: (error) => this.errorHandler.handleServerError(error.error)
        });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.customTableAttributeService.deleteCustomTableAttribute(id)
          .subscribe({
            next: () => this.router.navigate(['/customTableAttribute'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            }),
            error: (error) => {
              if (error.error?.code === 'REFERENCED') {
                const messageParts = error.error.message.split(',');
                this.router.navigate(['/customTableAttribute'], {
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
