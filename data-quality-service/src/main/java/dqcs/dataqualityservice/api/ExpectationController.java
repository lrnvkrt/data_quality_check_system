package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.expectation.ExpectationCreateRequest;
import dqcs.dataqualityservice.api.dto.expectation.ExpectationDto;
import dqcs.dataqualityservice.application.ExpectationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fields/{fieldId}/expectations")
@Tag(name = "Expectation")
public class ExpectationController {
    private final ExpectationService expectationService;


    @Autowired
    public ExpectationController(ExpectationService expectationService) {
        this.expectationService = expectationService;
    }

    @Operation(
            summary = "Создать правило для поля",
            description = "Создаёт новое правило валидации (expectation) для указанного поля данных",
            parameters = {
                    @Parameter(
                            name = "fieldId",
                            description = "UUID поля, к которому будет привязано правило",
                            required = true,
                            example = "6f27c814-b630-4fa0-8ee3-1fa24d3794e5"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Параметры создаваемого правила",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ExpectationCreateRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "UUID созданного правила"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации или параметрах запроса", content = @Content)
            }
    )
    @PostMapping
    public UUID create(
            @Valid
            @PathVariable UUID fieldId,
            @RequestBody ExpectationCreateRequest expectationCreateRequest
    ) {
        return expectationService.createExpectation(fieldId, expectationCreateRequest);
    }

    @Operation(
            summary = "Получить все правила для поля",
            description = "Возвращает список всех правил валидации, привязанных к конкретному полю данных",
            parameters = {
                    @Parameter(
                            name = "fieldId",
                            description = "UUID поля",
                            required = true,
                            example = "6f27c814-b630-4fa0-8ee3-1fa24d3794e5"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список правил",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExpectationDto.class)
                            )
                    )
            }
    )
    @GetMapping
    public List<ExpectationDto> getAll(@PathVariable UUID fieldId) {
        return expectationService.getExpectationsForFieldId(fieldId);
    }

}
