package dqcs.dataqualityservice.application;

import dqcs.dataqualityservice.api.dto.field.FieldCreateRequest;
import dqcs.dataqualityservice.api.dto.field.FieldDto;

import java.util.List;
import java.util.UUID;

public interface FieldService {
    UUID addField(UUID dataSourceId, FieldCreateRequest request);
    List<FieldDto> getFields(UUID dataSourceId);
    FieldDto getField(UUID id);
    void deleteField(UUID id);
}
