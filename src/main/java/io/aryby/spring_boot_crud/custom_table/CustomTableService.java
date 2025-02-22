package io.aryby.spring_boot_crud.custom_table;

import io.aryby.spring_boot_crud.custom_method.CustomMethod;
import io.aryby.spring_boot_crud.custom_method.CustomMethodRepository;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttribute;
import io.aryby.spring_boot_crud.custom_table_attributes.CustomTableAttributeRepository;
import io.aryby.spring_boot_crud.project_settings.ProjectSettings;
import io.aryby.spring_boot_crud.project_settings.ProjectSettingsRepository;
import io.aryby.spring_boot_crud.util.MyHelpper;
import io.aryby.spring_boot_crud.util.NotFoundException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomTableService {
private  final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CustomTableRepository customTableRepository;
    private final CustomTableAttributeRepository customTableAttributeRepository;
    private final ProjectSettingsRepository projectSettingRepository;
    private final CustomMethodRepository customMethodRepository;

    public CustomTableService(final CustomTableRepository customTableRepository,
            final CustomTableAttributeRepository customTableAttributeRepository,
            final ProjectSettingsRepository projectSettingRepository,
            final CustomMethodRepository customMethodRepository) {
        this.customTableRepository = customTableRepository;
        this.customTableAttributeRepository = customTableAttributeRepository;
        this.projectSettingRepository = projectSettingRepository;
        this.customMethodRepository = customMethodRepository;
    }

    public List<CustomTableAttribute>findAllAttributesByTableId(Long id){
        logger.info("customTableAttributeRepository.findAllByCustomTable({}) called", id);
        return customTableAttributeRepository.findAllByCustomTable(id);
    }

    public List<CustomTableDTO> findAll() {
        final List<CustomTable> customTables = customTableRepository.findAll(Sort.by("id"));
        return customTables.stream()
                .map(customTable -> mapToDTO(customTable, new CustomTableDTO()))
                .toList();
    }
    public List<CustomTableDTO> findAllByProjectSetting(Long projectSettingId) {
        final List<CustomTable> customTables = customTableRepository.findAllByprojectSetting(projectSettingId);
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
        customTableDTO.setName(MyHelpper.capitalizeFirstLetter(customTableDTO.getName()));
        final CustomTable customTable = new CustomTable();
        mapToEntity(customTableDTO, customTable);
        return customTableRepository.save(customTable).getId();
    }

    public void update(final Long id, final CustomTableDTO customTableDTO) {
        final CustomTable customTable = formateCustomTable(id, customTableDTO);
                customTableRepository.save(customTable);
    }
    private CustomTable formateCustomTable(final Long id, CustomTableDTO customTableDTO){
        CustomTable customTbl = customTableRepository.findById(id).orElseThrow(NotFoundException::new);

        if (customTableDTO.getName() !=null){
            customTbl.setName(customTableDTO.getName());
        }
        return customTbl;
    }

    public void delete(final Long id) {
        customTableRepository.deleteById(id);
    }

    private CustomTableDTO mapToDTO(final CustomTable customTable,
            final CustomTableDTO customTableDTO) {
        customTableDTO.setId(customTable.getId());
        customTableDTO.setName(customTable.getName());
       customTableDTO.setCustomTablesAttributes(
           customTableAttributeRepository.
               findAllByCustomTable(customTable.getId()));
        return customTableDTO;
    }

    private CustomTable mapToEntity(final CustomTableDTO customTableDTO,
            final CustomTable customTable) {
        customTable.setName(customTableDTO.getName());

        final ProjectSettings projectSetting = customTableDTO.getProjectSetting() == null ? null : projectSettingRepository.findById(customTableDTO.getProjectSetting())
                .orElseThrow(() -> new NotFoundException("projectSetting not found"));
        customTable.setProjectSetting(projectSetting.getId());
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
