import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NavigationEnd, Router, RouterLink} from '@angular/router';
import {Subscription} from 'rxjs';
import {ErrorHandler} from 'app/common/error-handler.injectable';
import {CustomTableService} from 'app/core/custom-table/custom-table.service';
import {CustomTableDTO} from 'app/core/custom-table/custom-table.model';
import {environment} from "../../../environments/environment";
import {CustomTableAttributeDTO} from "../custom-table-attributes/custom-table-attributes.model";
import {CustomTableAttributeService} from "../custom-table-attributes/custom-table-attributes.service";


@Component({
  selector: 'app-custom-table-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './custom-table-list.component.html'
})
export class CustomTableListComponent implements OnInit, OnDestroy {

  customTableService = inject(CustomTableService);
  errorHandler = inject(ErrorHandler);
  router = inject(Router);
  customTables?: CustomTableDTO[];
  navigationSubscription?: Subscription;

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      confirm: $localize`:@@delete.confirm:Do you really want to delete this element? This cannot be undone.`,
      deleted: $localize`:@@customTable.delete.success:Custom Table was removed successfully.`,
      'customTable.customMethod.customTable.referenced': $localize`:@@customTable.customMethod.customTable.referenced:This entity is still referenced by Custom Method ${details?.id} via field Custom Table.`
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

  customTableAttributeDTOs: CustomTableAttributeDTO[] = [];
  customTableAttributeService = new CustomTableAttributeService();

  ngOnDestroy() {
    this.navigationSubscription!.unsubscribe();
  }

  loadData() {

    this.customTableService.customTables$
      .subscribe({
        next: (data) => {
          console.log("this.customTables");
          console.log(data);
          this.customTables = data;
          this.customTables.forEach(customTable => {
            if (customTable.customTablesAttributes)
              customTable.customTablesAttributes.forEach((customTableAttribute) => {
                if (customTableAttribute) {
                  this.customTableAttributeService.getCustomTableAttribute(customTableAttribute).subscribe({
                    next: (data) => {
                      console.log("this.customTableAttributeDTOs");
                      console.log(data);
                      this.customTableAttributeDTOs.push(data);
                      console.log(data);
                    },
                    error: error => {
                      console.log(error);
                    }
                  });
                }
              })


          });

        },
        error: (error) => this.errorHandler.handleServerError(error.error)
      });
  }

  confirmDelete(id: number) {
    if (confirm(this.getMessage('confirm'))) {
      this.customTableService.deleteCustomTable(id)
        .subscribe({
          next: () => {
            localStorage.removeItem(environment.entityTable);
            this.router.navigate(['/customTables'], {
              state: {
                msgInfo: this.getMessage('deleted')
              }
            })
          },
          error: (error) => {
            if (error.error?.code === 'REFERENCED') {
              const messageParts = error.error.message.split(',');
              this.router.navigate(['/customTables'], {
                state: {
                  msgError: this.getMessage(messageParts[0], {id: messageParts[1]})
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
