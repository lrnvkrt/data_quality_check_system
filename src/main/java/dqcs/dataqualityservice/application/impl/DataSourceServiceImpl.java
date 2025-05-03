package dqcs.dataqualityservice.application.impl;

import dqcs.dataqualityservice.api.dto.DataSourceCreateRequest;
import dqcs.dataqualityservice.api.dto.DataSourceDto;
import dqcs.dataqualityservice.application.DataSourceService;
import dqcs.dataqualityservice.infrastructure.entity.DataSource;
import dqcs.dataqualityservice.infrastructure.repository.DataSourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DataSourceServiceImpl implements DataSourceService {

    private final Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

    private final DataSourceRepository dataSourceRepository;

    @Autowired
    public DataSourceServiceImpl(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public UUID create(DataSourceCreateRequest request) {
        logger.info("[create] Creating new DataSource");

        DataSource ds = new DataSource();
        ds.setId(UUID.randomUUID());
        ds.setName(request.name());
        ds.setDescription(request.description());
        ds.setCreatedAt(LocalDateTime.now());
        return dataSourceRepository.save(ds).getId();
    }

    @Override
    public void delete(UUID id) {
        logger.info("[delete] Deleting DataSource");
        dataSourceRepository.deleteById(id);
    }

    @Override
    public DataSourceDto get(UUID id) {
        logger.info("[get] Retrieving DataSource");
        DataSource ds = dataSourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DataSource not found"));

        return mapToDto(ds);
    }

    @Override
    public List<DataSourceDto> getAll() {
        logger.info("[getAll] Retrieving all DataSources");
        return dataSourceRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }


    private DataSourceDto mapToDto(DataSource ds) {
        return new DataSourceDto(ds.getId(), ds.getName(), ds.getDescription());
    }
}
