package io.aryby.spring_boot_crud.database_settings;

import io.aryby.spring_boot_crud.util.ReferencedException;
import io.aryby.spring_boot_crud.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin("*")
@RequestMapping(value = "/api/databaseSettingss", produces = MediaType.APPLICATION_JSON_VALUE)
public class DatabaseSettingsResource {

    private final DatabaseSettingsService databaseSettingsService;

    public DatabaseSettingsResource(final DatabaseSettingsService databaseSettingsService) {
        this.databaseSettingsService = databaseSettingsService;
    }

    @GetMapping
    public ResponseEntity<List<DatabaseSettingsDTO>> getAllDatabaseSettingss() {
        return ResponseEntity.ok(databaseSettingsService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatabaseSettingsDTO> getDatabaseSettings(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(databaseSettingsService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDatabaseSettings(
            @RequestBody @Valid final DatabaseSettingsDTO databaseSettingsDTO) {
        final Long createdId = databaseSettingsService.create(databaseSettingsDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateDatabaseSettings(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DatabaseSettingsDTO databaseSettingsDTO) {
        databaseSettingsService.update(id, databaseSettingsDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDatabaseSettings(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = databaseSettingsService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        databaseSettingsService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
