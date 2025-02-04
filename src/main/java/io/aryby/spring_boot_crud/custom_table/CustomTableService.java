package io.aryby.spring_boot_crud.custom_table;

import io.aryby.spring_boot_crud.custom_method.CustomMethod;
import io.aryby.spring_boot_crud.custom_method.CustomMethodRepository;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributes;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributesRepository;
import io.aryby.spring_boot_crud.project_settings.projectSetting;
import io.aryby.spring_boot_crud.project_settings.projectSettingRepository;
import io.aryby.spring_boot_crud.util.NotFoundException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomTableService {

    private final CustomTableRepository customTableRepository;
    private final CustomTableAttributesRepository customTableAttributesRepository;
    private final projectSettingRepository projectSettingRepository;
    private final CustomMethodRepository customMethodRepository;

    public CustomTableService(final CustomTableRepository customTableRepository,
            final CustomTableAttributesRepository customTableAttributesRepository,
            final projectSettingRepository projectSettingRepository,
            final CustomMethodRepository customMethodRepository) {
        this.customTableRepository = customTableRepository;
        this.customTableAttributesRepository = customTableAttributesRepository;
        this.projectSettingRepository = projectSettingRepository;
        this.customMethodRepository = customMethodRepository;
    }

    public List<CustomTableAttributes>findAllAttributesByTableId(Long id){
        return customTableAttributesRepository.findByCustomTable(id);
    }

    public List<CustomTableDTO> findAll() {
        final List<CustomTable> customTables = customTableRepository.findAll(Sort.by("id"));
        return customTables.stream()
                .map(customTable -> mapToDTO(customTable, new CustomTableDTO()))
                .toList();
    }

    public CustomTableDTO get(final Long id) {
        return customTableRepository.findById(id)
                .map(customTable -> mapToDTO(customTable, new CustomTableDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CustomTableDTO customTableDTO) {
        final CustomTable customTable = new CustomTable();
        mapToEntity(customTableDTO, customTable);
        return customTableRepository.save(customTable).getId();
    }

    public void update(final Long id, final CustomTableDTO customTableDTO) {
        final CustomTable customTable = customTableRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customTableDTO, customTable);
        customTableRepository.save(customTable);
    }

    public void delete(final Long id) {
        customTableRepository.deleteById(id);
    }

    private CustomTableDTO mapToDTO(final CustomTable customTable,
            final CustomTableDTO customTableDTO) {
        customTableDTO.setId(customTable.getId());
        customTableDTO.setName(customTable.getName());
       customTableDTO.setCustomTablesAttributes(
           customTableAttributesRepository.
               findByCustomTable(customTable.getId()));
        return customTableDTO;
    }

    private CustomTable mapToEntity(final CustomTableDTO customTableDTO,
            final CustomTable customTable) {
        customTable.setName(customTableDTO.getName());

        final projectSetting projectSetting = customTableDTO.getprojectSetting() == null ? null : projectSettingRepository.findById(customTableDTO.getprojectSetting())
                .orElseThrow(() -> new NotFoundException("projectSetting not found"));
        customTable.setprojectSetting(projectSetting.getId());
        return customTable;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final CustomTable customTable = customTableRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final CustomMethod customTableCustomMethod = customMethodRepository.findFirstByCustomTable(customTable);
        if (customTableCustomMethod != null) {
            referencedWarning.setKey("customTable.customMethod.customTable.referenced");
            referencedWarning.addParam(customTableCustomMethod.getId());
            return referencedWarning;
        }
        return null;
    }

}
