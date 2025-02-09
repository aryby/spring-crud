package io.aryby.spring_boot_crud.custom_table_attributes;

import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.util.CapitalizeFirstChar;
import io.aryby.spring_boot_crud.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomTableAttributeService {

    private final CustomTableAttributeRepository customTableAttributeRepository;

    public CustomTableAttributeService(
            final CustomTableAttributeRepository customTableAttributeRepository,
            final CustomTableRepository customTableRepository) {
        this.customTableAttributeRepository = customTableAttributeRepository;
    }

    public List<CustomTableAttributeDTO> findAll() {
        final List<CustomTableAttribute> customTableAttributees = customTableAttributeRepository.findAll(Sort.by("id"));
        return customTableAttributees.stream()
                .map(customTableAttribute -> mapToDTO(customTableAttribute, new CustomTableAttributeDTO()))
                .toList();
    }

    public List<CustomTableAttributeDTO> findAllByTableId(final Long tableId) {
        final List<CustomTableAttribute> customTableAttributees = customTableAttributeRepository.findAllByCustomTable(tableId);
        return customTableAttributees.stream()
            .map(customTableAttribute -> mapToDTO(customTableAttribute, new CustomTableAttributeDTO()))
            .toList();
    }

    public CustomTableAttributeDTO get(final Long id) {
        return customTableAttributeRepository.findById(id)
                .map(customTableAttribute -> mapToDTO(customTableAttribute, new CustomTableAttributeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CustomTableAttributeDTO customTableAttributeDTO) {

        if (customTableAttributeDTO.getNameTypeModifier().equalsIgnoreCase("boolean")) {

            if (!customTableAttributeDTO.getNameAttribute().substring(0, 2).equalsIgnoreCase("is")) {
                customTableAttributeDTO.setNameAttribute("is"+ CapitalizeFirstChar.capitalizeFirstLetter(customTableAttributeDTO.getNameAttribute()));
            }
        }
        final CustomTableAttribute customTableAttribute = new CustomTableAttribute();
        mapToEntity(customTableAttributeDTO, customTableAttribute);
        return customTableAttributeRepository.save(customTableAttribute).getId();
    }

    public void update(final Long id, final CustomTableAttributeDTO customTableAttributeDTO) {
        final CustomTableAttribute customTableAttribute = customTableAttributeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customTableAttributeDTO, customTableAttribute);
        customTableAttributeRepository.save(customTableAttribute);
    }

    public void delete(final Long id) {
        customTableAttributeRepository.deleteById(id);
    }

    private CustomTableAttributeDTO mapToDTO(final CustomTableAttribute customTableAttribute,
            final CustomTableAttributeDTO customTableAttributeDTO) {
        customTableAttributeDTO.setId(customTableAttribute.getId());
        customTableAttributeDTO.setNameTypeModifier(customTableAttribute.getNameTypeModifier());
        customTableAttributeDTO.setNameAttribute(customTableAttribute.getNameAttribute());
        customTableAttributeDTO.setSizeJpaAttributes(customTableAttribute.getSizeJpaAttributes());
        customTableAttributeDTO.setCustomJoins(customTableAttribute.getCustomJoins());
        customTableAttributeDTO.setCustomTable(customTableAttribute.getCustomTable());
        customTableAttributeDTO.setCustomRelations(customTableAttribute.getCustomRelations());
        return customTableAttributeDTO;
    }

    private CustomTableAttribute mapToEntity(
            final CustomTableAttributeDTO customTableAttributeDTO,
            final CustomTableAttribute customTableAttribute) {
        customTableAttribute.setNameTypeModifier(customTableAttributeDTO.getNameTypeModifier());
        customTableAttribute.setNameAttribute(customTableAttributeDTO.getNameAttribute());
        customTableAttribute.setCustomTable(customTableAttributeDTO.getCustomTable());
        customTableAttribute.setSizeJpaAttributes(customTableAttributeDTO.getSizeJpaAttributes());
        customTableAttribute.setCustomJoins(customTableAttributeDTO.getCustomJoins());
        customTableAttribute.setCustomRelations(customTableAttributeDTO.getCustomRelations());
        return customTableAttribute;
    }



}
