package dqcs.dataqualityservice.application.impl;

import dqcs.dataqualityservice.api.dto.DataSourceCreateRequest;
import dqcs.dataqualityservice.api.dto.DataSourceDto;
import dqcs.dataqualityservice.infrastructure.entity.DataSource;
import dqcs.dataqualityservice.infrastructure.repository.DataSourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DataSourceServiceImpl Unit Tests")
class DataSourceServiceImplTest {

    @Mock
    private DataSourceRepository dataSourceRepository;

    @InjectMocks
    private DataSourceServiceImpl dataSourceService;

    private UUID dataSourceId;

    @BeforeEach
    void setUp() {
        dataSourceId = UUID.randomUUID();
    }

    @Test
    @DisplayName("create() should save a new data source and return its ID")
    void shouldCreateDataSource() {
        DataSourceCreateRequest request = new DataSourceCreateRequest("my-source", "description");

        ArgumentCaptor<DataSource> captor = ArgumentCaptor.forClass(DataSource.class);
        when(dataSourceRepository.save(any())).thenAnswer(invocation -> {
            DataSource saved = invocation.getArgument(0);
            saved.setId(dataSourceId);
            return saved;
        });

        UUID result = dataSourceService.create(request);

        verify(dataSourceRepository).save(captor.capture());
        DataSource captured = captor.getValue();

        assertThat(result).isEqualTo(dataSourceId);
        assertThat(captured.getName()).isEqualTo("my-source");
        assertThat(captured.getDescription()).isEqualTo("description");
        assertThat(captured.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("delete() should call repository to delete by ID")
    void shouldDeleteDataSource() {
        dataSourceService.delete(dataSourceId);
        verify(dataSourceRepository).deleteById(dataSourceId);
    }

    @Test
    @DisplayName("get() should return DTO when data source exists")
    void shouldGetDataSourceById() {
        DataSource entity = new DataSource();
        entity.setId(dataSourceId);
        entity.setName("source1");
        entity.setDescription("desc");

        when(dataSourceRepository.findById(dataSourceId)).thenReturn(Optional.of(entity));

        DataSourceDto result = dataSourceService.get(dataSourceId);

        assertThat(result.id()).isEqualTo(dataSourceId);
        assertThat(result.name()).isEqualTo("source1");
        assertThat(result.description()).isEqualTo("desc");
    }

    @Test
    @DisplayName("get() should throw if data source not found")
    void shouldThrowWhenDataSourceNotFound() {
        when(dataSourceRepository.findById(dataSourceId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dataSourceService.get(dataSourceId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("DataSource not found");
    }

    @Test
    @DisplayName("getAll() should return list of DTOs")
    void shouldReturnAllDataSources() {
        DataSource ds1 = new DataSource();
        ds1.setId(UUID.randomUUID());
        ds1.setName("s1");
        ds1.setDescription("d1");

        DataSource ds2 = new DataSource();
        ds2.setId(UUID.randomUUID());
        ds2.setName("s2");
        ds2.setDescription("d2");

        when(dataSourceRepository.findAll()).thenReturn(List.of(ds1, ds2));

        List<DataSourceDto> result = dataSourceService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(DataSourceDto::name).containsExactly("s1", "s2");
    }
}