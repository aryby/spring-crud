package io.aryby.spring_boot_crud.custom_table_attributes;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.util.NotFoundException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomTableAttributesService {

    private final CustomTableAttributesRepository customTableAttributesRepository;
    private final CustomTableRepository customTableRepository;

    public CustomTableAttributesService(
            final CustomTableAttributesRepository customTableAttributesRepository,
            final CustomTableRepository customTableRepository) {
        this.customTableAttributesRepository = customTableAttributesRepository;
        this.customTableRepository = customTableRepository;
    }

    public List<CustomTableAttributesDTO> findAll() {
        final List<CustomTableAttributes> customTableAttributeses = customTableAttributesRepository.findAll(Sort.by("id"));
        return customTableAttributeses.stream()
                .map(customTableAttributes -> mapToDTO(customTableAttributes, new CustomTableAttributesDTO()))
                .toList();
    }

    public List<CustomTableAttributesDTO> findAllByTableId(final Long tableId) {
        final List<CustomTableAttributes> customTableAttributeses = customTableAttributesRepository.findByCustomTable(tableId);
        return customTableAttributeses.stream()
            .map(customTableAttributes -> mapToDTO(customTableAttributes, new CustomTableAttributesDTO()))
            .toList();
    }

    public CustomTableAttributesDTO get(final Long id) {
        return customTableAttributesRepository.findById(id)
                .map(customTableAttributes -> mapToDTO(customTableAttributes, new CustomTableAttributesDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CustomTableAttributesDTO customTableAttributesDTO) {
        final CustomTableAttributes customTableAttributes = new CustomTableAttributes();
        mapToEntity(customTableAttributesDTO, customTableAttributes);
        return customTableAttributesRepository.save(customTableAttributes).getId();
    }

    public void update(final Long id, final CustomTableAttributesDTO customTableAttributesDTO) {
        final CustomTableAttributes customTableAttributes = customTableAttributesRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customTableAttributesDTO, customTableAttributes);
        customTableAttributesRepository.save(customTableAttributes);
    }

    public void delete(final Long id) {
        customTableAttributesRepository.deleteById(id);
    }

    private CustomTableAttributesDTO mapToDTO(final CustomTableAttributes customTableAttributes,
            final CustomTableAttributesDTO customTableAttributesDTO) {
        customTableAttributesDTO.setId(customTableAttributes.getId());
        customTableAttributesDTO.setNameTypeModifier(customTableAttributes.getNameTypeModifier());
        customTableAttributesDTO.setNameAttribute(customTableAttributes.getNameAttribute());
        customTableAttributesDTO.setSizeJpaAttributes(customTableAttributes.getSizeJpaAttributes());
        customTableAttributesDTO.setCustomJoins(customTableAttributes.getCustomJoins());
        customTableAttributesDTO.setCustomTable(customTableAttributes.getCustomTable());
        customTableAttributesDTO.setCustomRelations(customTableAttributes.getCustomRelations());
        return customTableAttributesDTO;
    }

    private CustomTableAttributes mapToEntity(
            final CustomTableAttributesDTO customTableAttributesDTO,
            final CustomTableAttributes customTableAttributes) {
        customTableAttributes.setNameTypeModifier(customTableAttributesDTO.getNameTypeModifier());
        customTableAttributes.setNameAttribute(customTableAttributesDTO.getNameAttribute());
        customTableAttributes.setCustomTable(customTableAttributesDTO.getCustomTable());
        customTableAttributes.setSizeJpaAttributes(customTableAttributesDTO.getSizeJpaAttributes());
        customTableAttributes.setCustomJoins(customTableAttributesDTO.getCustomJoins());
        customTableAttributes.setCustomRelations(customTableAttributesDTO.getCustomRelations());
        return customTableAttributes;
    }



}
