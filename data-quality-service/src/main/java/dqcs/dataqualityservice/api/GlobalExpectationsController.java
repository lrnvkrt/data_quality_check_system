package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.expectation.ExpectationDto;
import dqcs.dataqualityservice.application.ExpectationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/expectations")
@Tag(name = "Expectation")
public class GlobalExpectationsController {

    private final ExpectationService expectationService;

    @Autowired
    public GlobalExpectationsController(ExpectationService expectationService) {
        this.expectationService = expectationService;
    }

    @Operation(
            summary = "Получить правило по ID",
            description = "Возвращает полную информацию о правиле валидации по его UUID",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "UUID правила",
                            required = true,
                            example = "f4a3bdef-3f88-43d1-bf6e-1f914afc7dcd"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Правило найдено", content = @Content(schema = @Schema(implementation = ExpectationDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Правило не найдено", content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ExpectationDto get(@PathVariable UUID id) {
        return expectationService.getExpectation(id);
    }

    @Operation(
            summary = "Удалить правило по ID",
            description = "Удаляет правило валидации по его UUID",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "UUID правила",
                            required = true,
                            example = "f4a3bdef-3f88-43d1-bf6e-1f914afc7dcd"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Правило успешно удалено"),
                    @ApiResponse(responseCode = "404", description = "Правило не найдено", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        expectationService.deleteExpectation(id);
    }

    @Operation(
            summary = "Переключить активность правила",
            description = "Включает или отключает правило валидации по его UUID",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "UUID правила",
                            required = true,
                            example = "f4a3bdef-3f88-43d1-bf6e-1f914afc7dcd"
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статус правила переключён"),
                    @ApiResponse(responseCode = "404", description = "Правило не найдено", content = @Content)
            }
    )
    @PostMapping("/{id}/toggle")
    public void toggle(@PathVariable UUID id) {
        expectationService.toggleExpectation(id);
    }
}
