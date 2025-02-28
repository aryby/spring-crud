package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettings;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.frontend.IMvcComponentList;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MvcComponentListImpl implements IMvcComponentList {
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    public MvcComponentListImpl(ProjectSettingsRepository projectSettingsRepository, GeneralSettingsRepository generalSettingsRepository, CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generate(CustomTable table, Long projectId) {
        System.out.println("Generating Angular List Component for: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));

        generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        String entityName = MyHelpper.capitalizeFirstLetter(table.getName());
        String lowerEntityName = MyHelpper.lowerCaseFirstLetter(table.getName());
        List<CustomTableAttributeDTO> attributes = customTableAttributeService.findAllByTableId(table.getId());

        StringBuilder sb = new StringBuilder();

        // Angular Template for List Component
        sb.append(String.format("""
        <div *ngIf="!%1$sEntity || %1$sEntity.length === 0">
            <div>No data found for %1$s.</div>
        </div>
        <div *ngIf="%1$sEntity && %1$sEntity.length > 0" class="overflow-x-auto">
            <h2 class="text-2xl font-bold mb-4">%1$s List</h2>
            <table class="w-full table-auto">
                <thead>
                    <tr>
        """, lowerEntityName));

        // Generate dynamic table headers
        attributes.forEach(attr -> sb.append(String.format("""
                        <th scope="col" class="text-left p-2">%s</th>
        """, MyHelpper.capitalizeFirstLetter(attr.getNameAttribute()))));

        // Add Actions Column
        sb.append("""
                        <th scope="col" class="text-left p-2">Actions</th>
                    </tr>
                </thead>
                <tbody class="border-t-2 border-gray-300">
                    <tr *ngFor="let item of %sEntity;" class="odd:bg-gray-100">
        """.formatted(lowerEntityName));

        // Generate dynamic table data cells
        attributes.forEach(attr -> sb.append(String.format("""
                        <td class="p-2">{{ item.%s }}</td>
        """, attr.getNameAttribute())));

        // Add Edit and Delete Actions
        sb.append(String.format("""
                        <td class="p-2">
                            <div class="flex space-x-2 justify-end">
                                <a [routerLink]="['/%s/edit', item.id]" class="px-3 py-1 text-white bg-blue-600 hover:bg-blue-700 rounded">
                                    Edit
                                </a>
                                <button (click)="confirmDelete(item.id)" class="px-3 py-1 text-white bg-red-500 hover:bg-red-600 rounded">
                                    Delete
                                </button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
        """, lowerEntityName));

        return sb.toString();
    }



}
