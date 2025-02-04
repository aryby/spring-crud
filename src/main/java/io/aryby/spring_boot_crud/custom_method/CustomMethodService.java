package io.aryby.spring_boot_crud.custom_method;

import io.aryby.spring_boot_crud.custom_table.CustomTable;
import io.aryby.spring_boot_crud.custom_table.CustomTableRepository;
import io.aryby.spring_boot_crud.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomMethodService {

    private final CustomMethodRepository customMethodRepository;
    private final CustomTableRepository customTableRepository;

    public CustomMethodService(final CustomMethodRepository customMethodRepository,
            final CustomTableRepository customTableRepository) {
        this.customMethodRepository = customMethodRepository;
        this.customTableRepository = customTableRepository;
    }

    public List<CustomMethodDTO> findAll() {
        final List<CustomMethod> customMethods = customMethodRepository.findAll(Sort.by("id"));
        return customMethods.stream()
                .map(customMethod -> mapToDTO(customMethod, new CustomMethodDTO()))
                .toList();
    }

    public CustomMethodDTO get(final Long id) {
        return customMethodRepository.findById(id)
                .map(customMethod -> mapToDTO(customMethod, new CustomMethodDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CustomMethodDTO customMethodDTO) {
        final CustomMethod customMethod = new CustomMethod();
        mapToEntity(customMethodDTO, customMethod);
        return customMethodRepository.save(customMethod).getId();
    }

    public void update(final Long id, final CustomMethodDTO customMethodDTO) {
        final CustomMethod customMethod = customMethodRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customMethodDTO, customMethod);
        customMethodRepository.save(customMethod);
    }

    public void delete(final Long id) {
        customMethodRepository.deleteById(id);
    }

    private CustomMethodDTO mapToDTO(final CustomMethod customMethod,
            final CustomMethodDTO customMethodDTO) {
        customMethodDTO.setId(customMethod.getId());
        customMethodDTO.setMethodName(customMethod.getMethodName());
        customMethodDTO.setMethodBody(customMethod.getMethodBody());
        customMethodDTO.setAnnotations(customMethod.getAnnotations());
        customMethodDTO.setCustomTable(customMethod.getCustomTable());
        return customMethodDTO;
    }

    private CustomMethod mapToEntity(final CustomMethodDTO customMethodDTO,
            final CustomMethod customMethod) {
        customMethod.setMethodName(customMethodDTO.getMethodName());
        customMethod.setMethodBody(customMethodDTO.getMethodBody());
        customMethod.setAnnotations(customMethodDTO.getAnnotations());
        customMethod.setCustomTable(customMethodDTO.getCustomTable());

        return customMethod;
    }

}
