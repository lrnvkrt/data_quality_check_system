package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.datasource.DataSourceCreateRequest;
import dqcs.dataqualityservice.api.dto.datasource.DataSourceDto;
import dqcs.dataqualityservice.application.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@RequestMapping("/datasources")
@Tag(name = "DataSource")
public class DataSourceController {

    private final DataSourceService dataSourceService;

    @Autowired
    public DataSourceController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @Operation(
            summary = "Создать источник данных",
            description = "Создает новый источник данных на основе переданных параметров, создает запись в БД и позволяет загружать сообщения по этому источнику",
            responses = {
                    @ApiResponse(responseCode = "200", description = "DataSource создан успешно, Возвращается UUID DataSource"),
                    @ApiResponse(responseCode = "400", description = "Ошибка валидации запроса")
            }
    )
    @PostMapping
    public UUID createDataSource(@Valid @RequestBody DataSourceCreateRequest ds) {
        return dataSourceService.create(ds);
    }

    @Operation(
            summary = "Список всех источников данных",
            description = "Возвращает полный список зарегистрированных источников данных",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешно", content = @Content(
                            array = @ArraySchema(schema = @Schema(implementation = DataSourceDto.class)))
                    )
            }
    )
    @GetMapping
    public List<DataSourceDto> list() {
        return dataSourceService.getAll();
    }

    @Operation(
            summary = "Получить источник по ID",
            description = "Возвращает информацию об источнике данных по его UUID",
            parameters = {
                    @Parameter(name = "id", description = "UUID источника данных", required = true, example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Источник найден", content = @Content(schema = @Schema(
                            contentMediaType = "application/json",
                            implementation = DataSourceDto.class))
                    ),
                    @ApiResponse(responseCode = "404", description = "Источник не найден", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public DataSourceDto findById(@PathVariable UUID id) {
        return dataSourceService.get(id);
    }

    @Operation(
            summary = "Удалить источник данных",
            description = "Удаляет источник данных по его UUID",
            parameters = {
                    @Parameter(name = "id", description = "UUID источника данных", required = true, example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Удаление успешно выполнено"),
                    @ApiResponse(responseCode = "404", description = "Источник не найден", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        dataSourceService.delete(id);
    }
}
