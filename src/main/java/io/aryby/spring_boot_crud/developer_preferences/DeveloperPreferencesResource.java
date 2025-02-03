package io.aryby.spring_boot_crud.developer_preferences;

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
@RequestMapping(value = "/api/developerPreferencess", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeveloperPreferencesResource {

    private final DeveloperPreferencesService developerPreferencesService;

    public DeveloperPreferencesResource(
            final DeveloperPreferencesService developerPreferencesService) {
        this.developerPreferencesService = developerPreferencesService;
    }

    @GetMapping
    public ResponseEntity<List<DeveloperPreferencesDTO>> getAllDeveloperPreferencess() {
        return ResponseEntity.ok(developerPreferencesService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeveloperPreferencesDTO> getDeveloperPreferences(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(developerPreferencesService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createDeveloperPreferences(
            @RequestBody @Valid final DeveloperPreferencesDTO developerPreferencesDTO) {
        final Long createdId = developerPreferencesService.create(developerPreferencesDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateDeveloperPreferences(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final DeveloperPreferencesDTO developerPreferencesDTO) {
        developerPreferencesService.update(id, developerPreferencesDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDeveloperPreferences(
            @PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = developerPreferencesService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        developerPreferencesService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
