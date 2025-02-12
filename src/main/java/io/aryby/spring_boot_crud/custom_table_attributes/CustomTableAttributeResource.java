package io.aryby.spring_boot_crud.custom_table_attributes;

import io.aryby.spring_boot_crud.util.JavaTypeMapper;
import io.aryby.spring_boot_crud.util.ReferencedException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/customTableAttributes", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomTableAttributeResource {

    private final CustomTableAttributeService customTableAttributeService;

    private final Logger logger = LoggerFactory.getLogger(CustomTableAttributeResource.class);

    public CustomTableAttributeResource(
            final CustomTableAttributeService customTableAttributeService) {
        this.customTableAttributeService = customTableAttributeService;
    }

    @GetMapping
    public ResponseEntity<List<CustomTableAttributeDTO>> getAllCustomTableAttributes() {
        return ResponseEntity.ok(customTableAttributeService.findAll());
    }

    @GetMapping("/customTable/{id}")
    public ResponseEntity<List<CustomTableAttributeDTO>> getAllCustomTableAttributeByTableId(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customTableAttributeService.findAllByTableId(id));
    }



    @GetMapping("/{id}")
    public ResponseEntity<CustomTableAttributeDTO> getCustomTableAttribute(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(customTableAttributeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCustomTableAttribute(
        @RequestBody @Valid final CustomTableAttributeDTO customTableAttributeDTO) {
        JavaTypeMapper.JavaType javaType = JavaTypeMapper.getJavaType(customTableAttributeDTO.getNameTypeModifier());
        customTableAttributeDTO.setNameTypeModifier(javaType.getFullQualifiedName());

        // Corrected logging
        logger.info("Converted Java Type Modifier: {}", customTableAttributeDTO);

        final Long createdId = customTableAttributeService.create(customTableAttributeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCustomTableAttribute(
        @PathVariable(name = "id") final Long id,
        @RequestBody @Valid final CustomTableAttributeDTO customTableAttributeDTO) {
        JavaTypeMapper.JavaType javaType = JavaTypeMapper.getJavaType(customTableAttributeDTO.getNameTypeModifier());
        customTableAttributeDTO.setNameTypeModifier(javaType.getFullQualifiedName());

        // Corrected logging
        logger.info("Update Converted Java Type Modifier: {}", customTableAttributeDTO);

        customTableAttributeService.update(id, customTableAttributeDTO);
        return ResponseEntity.ok(id);
    }


    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCustomTableAttribute(
            @PathVariable(name = "id") final Long id) {

        customTableAttributeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
