package io.aryby.spring_boot_crud.generator.frontend.implimentations;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeDTO;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeService;
import io.aryby.spring_boot_crud.general_settings.GeneralSettingsRepository;
import io.aryby.spring_boot_crud.generator.frontend.IMvcComponentCreate;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MvcComponentCreateImpl implements IMvcComponentCreate {

    private final Logger logger = LoggerFactory.getLogger(MvcComponentCreateImpl.class);
    private final ProjectSettingsRepository projectSettingsRepository;
    private final GeneralSettingsRepository generalSettingsRepository;
    private final CustomTableAttributeService customTableAttributeService;

    public MvcComponentCreateImpl(ProjectSettingsRepository projectSettingsRepository, GeneralSettingsRepository generalSettingsRepository, CustomTableAttributeService customTableAttributeService) {
        this.projectSettingsRepository = projectSettingsRepository;
        this.generalSettingsRepository = generalSettingsRepository;
        this.customTableAttributeService = customTableAttributeService;
    }

    @Override
    public String generate(CustomTable table, Long projectId) {
        logger.info("Generate Angular Create Component for: " + table.getName());

        ProjectSettings projectSetting = projectSettingsRepository.findById(projectId)
            .orElseThrow(() -> new IllegalArgumentException("ProjectSettings not found for ID: " + projectId));
        generalSettingsRepository.findById(projectSetting.getGeneralSettings())
            .orElseThrow(() -> new IllegalArgumentException("GeneralSettings not found for ProjectSettings ID: " + projectId));

        String entityName = MyHelpper.capitalizeFirstLetter(table.getName());
        String lowerEntityName = MyHelpper.lowerCaseFirstLetter(table.getName());
        List<CustomTableAttributeDTO> attributes = customTableAttributeService.findAllByTableId(table.getId());

        StringBuilder sb = new StringBuilder();

        // ✅ 1. إضافة النموذج مع ngSubmit
        sb.append(String.format("""
    <div class="p-4 bg-white shadow rounded">
        <h2 class="text-2xl font-bold mb-4">Ajouter %1$s</h2>

        <form (ngSubmit)="create()" #form="ngForm">
    """, entityName));

        // ✅ 2. إنشاء الحقول باستخدام ngModel
        attributes.forEach(attr -> sb.append(String.format("""
            <div class="mb-3">
                <label class="block text-sm font-medium text-gray-700">%1$s</label>
                <input type="text" class="mt-1 p-2 w-full border rounded" name="%2$s"
                    [(ngModel)]="%3$s.%2$s" required />
            </div>
    """,
            MyHelpper.capitalizeFirstLetter(attr.getNameAttribute()),
            attr.getNameAttribute(),
            lowerEntityName
        )));

        // ✅ 3. إضافة زر الحفظ
        sb.append("""
            <button type="submit" class="px-4 py-2 bg-blue-500 text-white rounded">Save</button>
        </form>
    </div>
    """);

        return sb.toString();
    }




}
