package dqcs.dataqualityservice.application.impl;

import dqcs.dataqualityservice.api.dto.FieldCreateRequest;
import dqcs.dataqualityservice.api.dto.FieldDto;
import dqcs.dataqualityservice.application.FieldService;
import dqcs.dataqualityservice.infrastructure.entity.DataSource;
import dqcs.dataqualityservice.infrastructure.entity.Field;
import dqcs.dataqualityservice.infrastructure.repository.DataSourceRepository;
import dqcs.dataqualityservice.infrastructure.repository.FieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FieldServiceImpl implements FieldService {

    private final Logger logger = LoggerFactory.getLogger(FieldServiceImpl.class);

    private final FieldRepository fieldRepository;
    private final DataSourceRepository dataSourceRepository;

    public FieldServiceImpl(FieldRepository fieldRepository, DataSourceRepository dataSourceRepository) {
        this.fieldRepository = fieldRepository;
        this.dataSourceRepository = dataSourceRepository;
    }


    @Override
    public UUID addField(UUID dataSourceId, FieldCreateRequest request) {
        logger.info("[addField] Creating new Field");

        DataSource dataSource = dataSourceRepository.findById(dataSourceId)
                .orElseThrow(() -> new RuntimeException("DataSource not found"));

        Field field = new Field();
        field.setId(UUID.randomUUID());
        field.setDataSource(dataSource);
        field.setName(request.name());
        field.setDataSource(dataSource);
        field.setDataType(request.dataType());

        return fieldRepository.save(field).getId();
    }

    @Override
    public List<FieldDto> getFields(UUID dataSourceId) {
        logger.info("[getFields] getting fields by DataSource {}", dataSourceId);
        return fieldRepository.findAllByDataSourceId(dataSourceId).stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public FieldDto getField(UUID id) {
        logger.info("[getField] getting field by id {}", id);
        return mapToDto(fieldRepository.findById(id).orElseThrow(() -> new RuntimeException("Field not found")));
    }

    @Override
    public void deleteField(UUID id) {
        logger.info("[deleteField] deleting field by id {}", id);
        fieldRepository.deleteById(id);
    }

    private FieldDto mapToDto(Field field) {
        return new FieldDto(field.getId(), field.getName(), field.getDataType());
    }
}
