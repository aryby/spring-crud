import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { CustomTableDTO } from 'app/core/custom-table/custom-table.model';
import { BehaviorSubject, Observable, map, tap } from 'rxjs';
import { transformRecordToMap } from 'app/common/utils';
import { CustomTableAttributesDTO } from '../custom-table-attributes/custom-table-attributes.model';

@Injectable({
  providedIn: 'root',
})
export class CustomTableService {
  private http = inject(HttpClient);
  private resourcePath = `${environment.apiPath}/api/customTables`;

  // **BehaviorSubjects for real-time updates**
  private customTablesSubject = new BehaviorSubject<CustomTableDTO[]>([]);
  customTables$ = this.customTablesSubject.asObservable();

  private customTableAttributesSubject = new BehaviorSubject<CustomTableAttributesDTO[]>([]);
  customTableAttributes$ = this.customTableAttributesSubject.asObservable();

  constructor() {
    this.loadAllCustomTables(); // Initial load
  }

  /**
   * Load all custom tables into the BehaviorSubject
   */
  private loadAllCustomTables() {
    this.http.get<CustomTableDTO[]>(this.resourcePath).subscribe({
      next: (data) => this.customTablesSubject.next(data),
      error: (error) => console.error('Error fetching custom tables:', error),
    });
  }

  /**
   * Load attributes for a specific table
   */
  loadCustomTableAttributesByTableId(id: number) {
    this.http.get<CustomTableAttributesDTO[]>(`${this.resourcePath}/customTable/${id}`).subscribe({
      next: (data) => this.customTableAttributesSubject.next(data),
      error: (error) => console.error('Error fetching table attributes:', error),
    });
  }

  /**
   * Fetch a single custom table by ID
   */
  getCustomTable(id: number): Observable<CustomTableDTO> {
    return this.http.get<CustomTableDTO>(`${this.resourcePath}/${id}`);
  }

  /**
   * Create a new custom table and update the BehaviorSubject
   */
  createCustomTable(customTableDTO: CustomTableDTO): Observable<number> {
    return this.http.post<number>(this.resourcePath, customTableDTO).pipe(
      tap(() => this.loadAllCustomTables()) // Reload data after creation
    );
  }

  /**
   * Update an existing custom table and refresh data
   */
  updateCustomTable(id: number, customTableDTO: CustomTableDTO): Observable<number> {
    return this.http.put<number>(`${this.resourcePath}/${id}`, customTableDTO).pipe(
      tap(() => this.loadAllCustomTables()) // Reload data after update
    );
  }

  /**
   * Delete a custom table and refresh the list
   */
  deleteCustomTable(id: number): Observable<void> {
    return this.http.delete<void>(`${this.resourcePath}/${id}`).pipe(
      tap(() => this.loadAllCustomTables()) // Reload data after deletion
    );
  }

  /**
   * Get project settings as a mapped object
   */
  getProjectSettingsValues() {
    return this.http.get<Record<string, number>>(`${this.resourcePath}/projectSettingsValues`).pipe(
      map(transformRecordToMap)
    );
  }
}
