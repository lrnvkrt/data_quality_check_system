package dqcs.dataqualityservice.application;

import dqcs.dataqualityservice.api.dto.DataSourceCreateRequest;
import dqcs.dataqualityservice.api.dto.DataSourceDto;

import java.util.List;
import java.util.UUID;

public interface DataSourceService {
    UUID create(DataSourceCreateRequest request);
    void delete(UUID id);
    DataSourceDto get(UUID id);
    List<DataSourceDto> getAll();
}
