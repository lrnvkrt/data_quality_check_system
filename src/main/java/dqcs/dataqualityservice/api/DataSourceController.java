package dqcs.dataqualityservice.api;

import dqcs.dataqualityservice.api.dto.DataSourceCreateRequest;
import dqcs.dataqualityservice.api.dto.DataSourceDto;
import dqcs.dataqualityservice.application.DataSourceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/datasources")
public class DataSourceController {

    private final DataSourceService dataSourceService;

    @Autowired
    public DataSourceController(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @PostMapping
    public UUID createDataSource(@Valid @RequestBody DataSourceCreateRequest ds) {
        return dataSourceService.create(ds);
    }

    @GetMapping
    public List<DataSourceDto> list() {
        return dataSourceService.getAll();
    }

    @GetMapping("/{id}")
    public DataSourceDto findById(@PathVariable UUID id) {
        return dataSourceService.get(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        dataSourceService.delete(id);
    }
}
