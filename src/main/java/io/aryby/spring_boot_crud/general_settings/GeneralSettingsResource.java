package io.aryby.spring_boot_crud.general_settings;

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
@RequestMapping(value = "/api/generalSettings", produces = MediaType.APPLICATION_JSON_VALUE)
public class GeneralSettingsResource {

    private final GeneralSettingsService generalSettingsService;
    private final Logger logger = LoggerFactory.getLogger(GeneralSettingsResource.class);

    public GeneralSettingsResource(final GeneralSettingsService generalSettingsService) {
        this.generalSettingsService = generalSettingsService;
    }

    @GetMapping
    public ResponseEntity<List<GeneralSettingsDTO>> getAllGeneralSettingss() {
        return ResponseEntity.ok(generalSettingsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralSettingsDTO> getGeneralSettings(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(generalSettingsService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createGeneralSettings(
            @RequestBody @Valid final GeneralSettingsDTO generalSettingsDTO) {
        logger.info("Recieved request GeneralSettingsDTO, "+generalSettingsDTO.toString());
        final Long createdId = generalSettingsService.create(generalSettingsDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateGeneralSettings(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final GeneralSettingsDTO generalSettingsDTO) {
        generalSettingsService.update(id, generalSettingsDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteGeneralSettings(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = generalSettingsService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        generalSettingsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
