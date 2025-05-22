package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.expectation.ExpectationCatalogDto;
import dqcs.dataqualityservice.application.ExpectationCatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/expectations/catalog")
@Tag(name = "Expectation")
public class ExpectationCatalogController {
    private final ExpectationCatalogService expectationCatalogService;

    @Autowired
    public ExpectationCatalogController(ExpectationCatalogService expectationCatalogService) {
        this.expectationCatalogService = expectationCatalogService;
    }

    @Operation(
            summary = "Получить список доступных правил",
            description = "Возвращает список всех правил валидации, доступных для использования при создании expectations",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение каталога правил",
                            content = @Content(
                                    mediaType = "application.json",
                                    array = @ArraySchema(schema = @Schema(implementation = ExpectationCatalogDto.class))
                            )
                    )
            }
    )
    @GetMapping
    public List<ExpectationCatalogDto> getExpectations() {
        return expectationCatalogService.getAll();
    }
}
