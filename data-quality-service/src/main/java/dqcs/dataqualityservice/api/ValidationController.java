package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.GenericEvent;
import dqcs.dataqualityservice.api.dto.expectation.ValidationResultDto;
import dqcs.dataqualityservice.application.BatchValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/batch")
@Tag(name = "Validation")
public class ValidationController {

    private final BatchValidationService batchValidationService;


    @Autowired
    public ValidationController(BatchValidationService batchValidationService) {
        this.batchValidationService = batchValidationService;
    }

    @Operation(
            summary = "Пакетная валидация событий",
            description = "Выполняет проверку набора событий (сообщений Kafka) для указанного топика. " +
                    "Возвращает результат выполнения всех expectations, настроенных на источник данных, связанный с топиком.",
            parameters = {
                    @Parameter(
                            name = "topic",
                            description = "Имя топика",
                            required = true,
                            example = "orders.topic"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Список событий с полезной нагрузкой (payload), представляющей строки данных",
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Запрос на валидацию принят",
                            content = @Content(schema = @Schema(implementation = ValidationResultDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Источник данных для указанного топика не найден",
                            content = @Content
                    )
            }
    )
    @PostMapping("/{topic}")
    public ValidationResultDto validate(
            @PathVariable String topic,
            @RequestBody List<GenericEvent> events
    ) {
        return batchValidationService.validate(topic, events);
    }
}
