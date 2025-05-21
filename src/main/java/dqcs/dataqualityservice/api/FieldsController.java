package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.FieldCreateRequest;
import dqcs.dataqualityservice.api.dto.FieldDto;
import dqcs.dataqualityservice.application.FieldService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/datasources/{dataSourceId}/fields")
public class FieldsController {

    private final FieldService fieldService;


    public FieldsController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @PostMapping
    public UUID addField(
            @Valid
            @PathVariable UUID dataSourceId,
            @RequestBody FieldCreateRequest request
    ) {
        return fieldService.addField(dataSourceId, request);
    }

    @GetMapping
    public List<FieldDto> getFieldsByDataSource(
            @PathVariable UUID dataSourceId
    ) {
        return fieldService.getFields(dataSourceId);
    }

    @DeleteMapping("/{fieldId}")
    public void deleteField(@PathVariable UUID fieldId) {
        fieldService.deleteField(fieldId);
    }

    @GetMapping("/{fieldId}")
    public FieldDto getField(@PathVariable UUID fieldId) {
        return fieldService.getField(fieldId);
    }
}
