package dqcs.dataqualityservice.application.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dqcs.dataqualityservice.api.dto.expectation.ExpectationCreateRequest;
import dqcs.dataqualityservice.api.dto.expectation.ExpectationDto;
import dqcs.dataqualityservice.application.ExpectationService;
import dqcs.dataqualityservice.application.exception.*;
import dqcs.dataqualityservice.infrastructure.entity.Expectation;
import dqcs.dataqualityservice.infrastructure.entity.ExpectationCatalog;
import dqcs.dataqualityservice.infrastructure.entity.Field;
import dqcs.dataqualityservice.infrastructure.entity.Severity;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationCatalogRepository;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationRepository;
import dqcs.dataqualityservice.infrastructure.repository.FieldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExpectationServiceImpl implements ExpectationService {

    private final Logger logger = LoggerFactory.getLogger(ExpectationServiceImpl.class);

    private final ExpectationRepository expectationRepository;
    private final FieldRepository fieldRepository;
    private final ExpectationCatalogRepository catalogRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ExpectationServiceImpl(ExpectationRepository expectationRepository, FieldRepository fieldRepository, ExpectationCatalogRepository catalogRepository, ObjectMapper objectMapper) {
        this.expectationRepository = expectationRepository;
        this.fieldRepository = fieldRepository;
        this.catalogRepository = catalogRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public UUID createExpectation(UUID fieldId, ExpectationCreateRequest request) {
        logger.info("[createExpectation] Creating expectation");

        Field field = fieldRepository.findById(fieldId).orElseThrow(
                () ->  new FieldNotFoundException("Field with id " + fieldId + " not found")
        );

        ExpectationCatalog catalog = catalogRepository.findById(request.expectationTypeId()).orElseThrow(
                () -> new ExpectationCatalogNotFoundException("Catalog with id " + request.expectationTypeId() + " not found")
        );

        Set<String> allowed = Set.copyOf(catalog.getAllowedKwargs() != null ? catalog.getAllowedKwargs(): List.of());
        Set<String> provided = request.kwargs() != null ? request.kwargs().keySet() : Set.of();

        if (!allowed.containsAll(provided)) {
            throw new InvalidKwargsException("Provided kwargs are not allowed: " + provided.stream()
                    .filter(kwarg -> !allowed.contains(kwarg)).toList());
        }

        if (catalog.isRequiresNumeric()) {
            String type = field.getDataType().toLowerCase();
            if (!type.contains("int") && !type.contains("float") && !type.contains("double") && !type.contains("long")) {
                throw new InvalidFieldTypeException("Type not supported: " + type);
            }
        }

        if (request.rowCondition() != null && !request.rowCondition().isBlank() && !catalog.isSupportsRowCondition()) {
            throw new InvalidKwargsException("Condition not supported: " + request.rowCondition());
        }

        Expectation expectation = new Expectation();
        expectation.setId(UUID.randomUUID());
        expectation.setField(field);
        expectation.setExpectationType(catalog);
        expectation.setDescription(request.description());
        expectation.setSeverity(Severity.valueOf(request.severity()));
        expectation.setRowCondition(request.rowCondition());
        expectation.setEnabled(request.enabled());
        expectation.setCreatedAt(LocalDateTime.now());
        expectation.setModifiedAt(null);

        try {
            expectation.setKwargs(objectMapper.writeValueAsString(request.kwargs()));
        } catch (Exception e) {
            throw new InvalidKwargsException("Kwargs could not be serialized to json: " + request.kwargs());
        }

        return expectationRepository.save(expectation).getId();
    }

    @Override
    public List<ExpectationDto> getExpectationsForFieldId(UUID fieldId) {
        logger.info("[getExpectationsForFieldId] Getting expectations for field: {}", fieldId);
        return expectationRepository.findAllByFieldId(fieldId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExpectationDto getExpectation(UUID expectationId) {
        logger.info("[getExpectation] Getting expectation: {}", expectationId);
        return expectationRepository.findById(expectationId).map(this::mapToDto).orElseThrow(
                () -> new ExpectationNotFoundException("Expectation with id " + expectationId + " not found")
        );
    }

    @Override
    public void deleteExpectation(UUID expectationId) {
        logger.info("[deleteExpectation] Deleting expectation: {}", expectationId);

        if (!expectationRepository.existsById(expectationId)) {
            throw new ExpectationNotFoundException("Expectation with id " + expectationId + " not found");
        }

        expectationRepository.deleteById(expectationId);
    }

    @Override
    public void toggleExpectation(UUID expectationId) {
        logger.info("[toggleExpectation] Updating expectation enabled: {}", expectationId);
        Expectation expectation = expectationRepository.findById(expectationId).orElseThrow(
                () -> new ExpectationNotFoundException("Expectation with id " + expectationId + " not found")
        );

        expectation.setEnabled(!expectation.isEnabled());
        expectation.setModifiedAt(LocalDateTime.now());

        expectationRepository.save(expectation);
    }

    private ExpectationDto mapToDto(Expectation e) {
        Map<String, Object> kwargs = Map.of();
        try {
            kwargs = objectMapper.readValue(e.getKwargs(), new TypeReference<>() {});
        } catch (Exception ignored) {}

        return new ExpectationDto(
                e.getId(),
                e.getField().getId(),
                e.getExpectationType().getId(),
                e.getExpectationType().getCode(),
                kwargs,
                e.getDescription(),
                e.getSeverity().toString(),
                e.getRowCondition(),
                e.isEnabled(),
                e.getCreatedAt(),
                e.getModifiedAt()
        );
    }
}
