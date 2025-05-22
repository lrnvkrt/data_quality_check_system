package dqcs.dataqualityservice.application.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dqcs.dataqualityservice.api.dto.expectation.ExpectationCreateRequest;
import dqcs.dataqualityservice.api.dto.expectation.ExpectationDto;
import dqcs.dataqualityservice.application.exception.*;
import dqcs.dataqualityservice.infrastructure.entity.Expectation;
import dqcs.dataqualityservice.infrastructure.entity.ExpectationCatalog;
import dqcs.dataqualityservice.infrastructure.entity.Field;
import dqcs.dataqualityservice.infrastructure.entity.Severity;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationCatalogRepository;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationRepository;
import dqcs.dataqualityservice.infrastructure.repository.FieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpectationServiceImplTest {

    @Mock
    private ExpectationRepository expectationRepository;
    @Mock
    private FieldRepository fieldRepository;
    @Mock
    private ExpectationCatalogRepository catalogRepository;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ExpectationServiceImpl service;

    private Field validField;
    private ExpectationCatalog validCatalog;

    @BeforeEach
    void setUp() {
        validField = new Field();
        validField.setId(UUID.randomUUID());
        validField.setDataType("Integer");

        validCatalog = new ExpectationCatalog();
        validCatalog.setId(UUID.randomUUID());
        validCatalog.setAllowedKwargs(List.of("min", "max"));
        validCatalog.setRequiresNumeric(true);
        validCatalog.setSupportsRowCondition(true);
    }

    @Test
    void createExpectation_successful() throws Exception {
        // given
        UUID fieldId = validField.getId();
        UUID catalogId = validCatalog.getId();
        Map<String, Object> kwargs = Map.of("min", 0, "max", 100);
        ExpectationCreateRequest request = new ExpectationCreateRequest(
                catalogId,
                kwargs,
                "desc",
                "col > 0",
                Severity.WARNING.toString(),
                true
        );

        when(fieldRepository.findById(fieldId)).thenReturn(Optional.of(validField));
        when(catalogRepository.findById(catalogId)).thenReturn(Optional.of(validCatalog));
        when(objectMapper.writeValueAsString(kwargs)).thenReturn("{\"min\":0,\"max\":100}");

        ArgumentCaptor<Expectation> captor = ArgumentCaptor.forClass(Expectation.class);
        Expectation saved = new Expectation();
        saved.setId(UUID.randomUUID());
        when(expectationRepository.save(captor.capture())).thenReturn(saved);

        // when
        UUID resultId = service.createExpectation(fieldId, request);

        // then
        assertThat(resultId).isEqualTo(saved.getId());

        Expectation toSave = captor.getValue();
        assertThat(toSave.getField()).isEqualTo(validField);
        assertThat(toSave.getExpectationType()).isEqualTo(validCatalog);
        assertThat(toSave.getDescription()).isEqualTo("desc");
        assertThat(toSave.getSeverity()).isEqualTo(Severity.WARNING);
        assertThat(toSave.getRowCondition()).isEqualTo("col > 0");
        assertThat(toSave.isEnabled()).isTrue();
        assertThat(toSave.getKwargs()).isEqualTo("{\"min\":0,\"max\":100}");
        assertThat(toSave.getCreatedAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    void createExpectation_fieldNotFound_throws() {
        UUID fieldId = UUID.randomUUID();
        when(fieldRepository.findById(fieldId)).thenReturn(Optional.empty());


        ExpectationCreateRequest req = new ExpectationCreateRequest(
                UUID.randomUUID(), Map.of(), "", Severity.CRITICAL.toString(), null, false
        );

        assertThatThrownBy(() -> service.createExpectation(fieldId, req))
                .isInstanceOf(FieldNotFoundException.class);
    }

    @Test
    void createExpectation_catalogNotFound_throws() {
        UUID fieldId = validField.getId();
        when(fieldRepository.findById(fieldId)).thenReturn(Optional.of(validField));
        when(catalogRepository.findById(validCatalog.getId())).thenReturn(Optional.empty());

        ExpectationCreateRequest req = new ExpectationCreateRequest(
                validCatalog.getId(), Map.of(), "", Severity.CRITICAL.toString(), null, false
        );

        assertThatThrownBy(() -> service.createExpectation(fieldId, req))
                .isInstanceOf(ExpectationCatalogNotFoundException.class);
    }

    @Test
    void createExpectation_invalidKwargs_throws() {
        UUID fieldId = validField.getId();
        when(fieldRepository.findById(fieldId)).thenReturn(Optional.of(validField));
        when(catalogRepository.findById(validCatalog.getId())).thenReturn(Optional.of(validCatalog));

        Map<String, Object> badArgs = Map.of("unknown", 1);
        ExpectationCreateRequest req = new ExpectationCreateRequest(
                validCatalog.getId(), badArgs, "", Severity.CRITICAL.toString(), null, false
        );

        assertThatThrownBy(() -> service.createExpectation(fieldId, req))
                .isInstanceOf(InvalidKwargsException.class);
    }

    @Test
    void createExpectation_nonNumericField_throws() {
        Field stringField = new Field();
        stringField.setId(UUID.randomUUID());
        stringField.setDataType("VARCHAR");

        when(fieldRepository.findById(stringField.getId())).thenReturn(Optional.of(stringField));
        when(catalogRepository.findById(validCatalog.getId())).thenReturn(Optional.of(validCatalog));

        ExpectationCreateRequest req = new ExpectationCreateRequest(
                validCatalog.getId(), Map.of(), "", Severity.CRITICAL.toString(), null, false
        );

        assertThatThrownBy(() -> service.createExpectation(stringField.getId(), req))
                .isInstanceOf(InvalidFieldTypeException.class);
    }

    @Test
    void createExpectation_rowConditionNotSupported_throws() {
        validCatalog.setSupportsRowCondition(false);
        when(fieldRepository.findById(validField.getId())).thenReturn(Optional.of(validField));
        when(catalogRepository.findById(validCatalog.getId())).thenReturn(Optional.of(validCatalog));

        ExpectationCreateRequest req = new ExpectationCreateRequest(
                validCatalog.getId(), Map.of(), "", Severity.CRITICAL.toString(), "row>0", false
        );

        assertThatThrownBy(() -> service.createExpectation(validField.getId(), req))
                .isInstanceOf(InvalidKwargsException.class);
    }

    @Test
    void getExpectationsForFieldId_returnsDtos() throws Exception {
        UUID fieldId = UUID.randomUUID();
        Expectation e = new Expectation();
        e.setId(UUID.randomUUID());
        e.setField(validField);
        e.setExpectationType(validCatalog);
        e.setDescription("d");
        e.setSeverity(Severity.CRITICAL);
        e.setRowCondition(null);
        e.setEnabled(false);
        e.setCreatedAt(LocalDateTime.now());
        e.setKwargs("{\"foo\":\"bar\"}");
        when(expectationRepository.findAllByFieldId(fieldId)).thenReturn(List.of(e));
        when(objectMapper.readValue(eq("{\"foo\":\"bar\"}"), ArgumentMatchers.<TypeReference<Map<String, Object>>>any()))
                .thenReturn(Map.of("foo", "bar"));

        List<ExpectationDto> dtos = service.getExpectationsForFieldId(fieldId);

        assertThat(dtos).hasSize(1);
        ExpectationDto dto = dtos.get(0);
        assertThat(dto.expectationId()).isEqualTo(e.getId());
        assertThat(dto.kwargs()).containsEntry("foo", "bar");
    }

    @Test
    void getExpectation_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(expectationRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getExpectation(id))
                .isInstanceOf(ExpectationNotFoundException.class);
    }

    @Test
    void deleteExpectation_invokesRepository() {
        UUID id = UUID.randomUUID();

        when(expectationRepository.existsById(id)).thenReturn(true);

        service.deleteExpectation(id);
        verify(expectationRepository).deleteById(id);
    }

    @Test
    void toggleExpectation_togglesAndSaves() {
        UUID id = UUID.randomUUID();
        Expectation e = new Expectation();
        e.setId(id);
        e.setEnabled(false);
        when(expectationRepository.findById(id)).thenReturn(Optional.of(e));

        service.toggleExpectation(id);

        assertThat(e.isEnabled()).isTrue();
        assertThat(e.getModifiedAt()).isBeforeOrEqualTo(LocalDateTime.now());
        verify(expectationRepository).save(e);
    }
}