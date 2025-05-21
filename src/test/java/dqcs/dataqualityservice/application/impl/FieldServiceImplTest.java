package dqcs.dataqualityservice.application.impl;

import dqcs.dataqualityservice.api.dto.FieldCreateRequest;
import dqcs.dataqualityservice.api.dto.FieldDto;
import dqcs.dataqualityservice.application.exception.DataSourceNotFoundException;
import dqcs.dataqualityservice.application.exception.FieldNotFoundException;
import dqcs.dataqualityservice.infrastructure.entity.DataSource;
import dqcs.dataqualityservice.infrastructure.entity.Field;
import dqcs.dataqualityservice.infrastructure.repository.DataSourceRepository;
import dqcs.dataqualityservice.infrastructure.repository.FieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FieldServiceImplTest {

    private FieldRepository fieldRepository;
    private DataSourceRepository dataSourceRepository;
    private FieldServiceImpl fieldService;

    @BeforeEach
    void setUp() {
        fieldRepository = mock(FieldRepository.class);
        dataSourceRepository = mock(DataSourceRepository.class);
        fieldService = new FieldServiceImpl(fieldRepository, dataSourceRepository);
    }

    @Nested
    @DisplayName("addField")
    class AddFieldTests {

        @Test
        @DisplayName("should create and return ID of new field when data source exists")
        void shouldAddFieldSuccessfully() {
            UUID dataSourceId = UUID.randomUUID();
            UUID fieldId = UUID.randomUUID();
            DataSource dataSource = new DataSource();
            FieldCreateRequest request = new FieldCreateRequest("testField", "STRING");

            Field savedField = new Field();
            savedField.setId(fieldId);

            when(dataSourceRepository.findById(dataSourceId)).thenReturn(Optional.of(dataSource));
            when(fieldRepository.save(any(Field.class))).thenReturn(savedField);

            UUID result = fieldService.addField(dataSourceId, request);

            assertThat(result).isEqualTo(fieldId);
            verify(fieldRepository).save(any(Field.class));
        }

        @Test
        @DisplayName("should throw if data source is not found")
        void shouldThrowIfDataSourceNotFound() {
            UUID dataSourceId = UUID.randomUUID();
            FieldCreateRequest request = new FieldCreateRequest("field", "STRING");

            when(dataSourceRepository.findById(dataSourceId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> fieldService.addField(dataSourceId, request))
                    .isInstanceOf(DataSourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("getFields")
    class GetFieldsTests {

        @Test
        @DisplayName("should return list of field DTOs for given data source")
        void shouldReturnFieldList() {
            UUID dataSourceId = UUID.randomUUID();
            Field field = new Field();
            field.setId(UUID.randomUUID());
            field.setName("field1");
            field.setDataType("STRING");

            when(fieldRepository.findAllByDataSourceId(dataSourceId)).thenReturn(List.of(field));

            List<FieldDto> result = fieldService.getFields(dataSourceId);

            assertThat(result)
                    .hasSize(1)
                    .first()
                    .satisfies(dto -> {
                        assertThat(dto.id()).isEqualTo(field.getId());
                        assertThat(dto.name()).isEqualTo("field1");
                        assertThat(dto.dataType()).isEqualTo("STRING");
                    });
        }
    }

    @Nested
    @DisplayName("getField")
    class GetFieldTests {

        @Test
        @DisplayName("should return field DTO if field exists")
        void shouldReturnFieldDto() {
            UUID fieldId = UUID.randomUUID();
            Field field = new Field();
            field.setId(fieldId);
            field.setName("fieldA");
            field.setDataType("INT");

            when(fieldRepository.findById(fieldId)).thenReturn(Optional.of(field));

            FieldDto result = fieldService.getField(fieldId);

            assertThat(result.id()).isEqualTo(fieldId);
            assertThat(result.name()).isEqualTo("fieldA");
            assertThat(result.dataType()).isEqualTo("INT");
        }

        @Test
        @DisplayName("should throw if field is not found")
        void shouldThrowIfFieldNotFound() {
            UUID fieldId = UUID.randomUUID();
            when(fieldRepository.findById(fieldId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> fieldService.getField(fieldId))
                    .isInstanceOf(FieldNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("deleteField")
    class DeleteFieldTests {

        @Test
        @DisplayName("should call deleteById on repository")
        void shouldDeleteField() {
            UUID fieldId = UUID.randomUUID();

            when(fieldRepository.existsById(fieldId)).thenReturn(true);

            fieldService.deleteField(fieldId);

            verify(fieldRepository).deleteById(fieldId);
        }
    }
}