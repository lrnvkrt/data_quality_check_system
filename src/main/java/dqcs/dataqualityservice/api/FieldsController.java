package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.field.FieldCreateRequest;
import dqcs.dataqualityservice.api.dto.field.FieldDto;
import dqcs.dataqualityservice.application.FieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/datasources/{dataSourceId}/fields")
@Tag(name = "Field")
public class FieldsController {

    private final FieldService fieldService;


    public FieldsController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    @Operation(
            summary = "Добавить поле в источник данных",
            description = "Создаёт новое поле в заданном источнике данных",
            parameters = {
                    @Parameter(
                            name = "dataSourceId",
                            description = "UUID источника данных",
                            required = true,
                            example = "3e0d0a7e-8a5d-4c39-b5f7-2aa8a8f4b123"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Описание добавляемого поля",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FieldCreateRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "UUID созданного поля"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса", content = @Content)
            }
    )
    @PostMapping
    public UUID addField(
            @Valid
            @PathVariable UUID dataSourceId,
            @RequestBody FieldCreateRequest request
    ) {
        return fieldService.addField(dataSourceId, request);
    }

    @Operation(
            summary = "Получить все поля по источнику данных",
            description = "Возвращает список всех полей, привязанных к указанному источнику данных",
            parameters = {
                    @Parameter(
                            name = "dataSourceId",
                            description = "UUID источника данных",
                            required = true,
                            example = "3e0d0a7e-8a5d-4c39-b5f7-2aa8a8f4b123"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список полей",
                            content = @Content(schema = @Schema(implementation = FieldDto.class))
                    )
            }
    )
    @GetMapping
    public List<FieldDto> getFieldsByDataSource(
            @PathVariable UUID dataSourceId
    ) {
        return fieldService.getFields(dataSourceId);
    }

    @Operation(
            summary = "Удалить поле",
            description = "Удаляет поле по его идентификатору",
            parameters = {
                    @Parameter(
                            name = "fieldId",
                            description = "UUID поля данных",
                            required = true,
                            example = "9432155e-47e4-4a1a-bdfc-b48ef2d33c11"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Поле успешно удалено"),
                    @ApiResponse(responseCode = "404", description = "Поле не найдено", content = @Content)
            }
    )
    @DeleteMapping("/{fieldId}")
    public void deleteField(@PathVariable UUID fieldId) {
        fieldService.deleteField(fieldId);
    }

    @Operation(
            summary = "Получить информацию о поле",
            description = "Возвращает информацию о поле по его UUID",
            parameters = {
                    @Parameter(
                            name = "fieldId",
                            description = "UUID поля данных",
                            required = true,
                            example = "9432155e-47e4-4a1a-bdfc-b48ef2d33c11"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о поле",
                            content = @Content(schema = @Schema(implementation = FieldDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Поле не найдено", content = @Content)
            }
    )
    @GetMapping("/{fieldId}")
    public FieldDto getField(@PathVariable UUID fieldId) {
        return fieldService.getField(fieldId);
    }
}
