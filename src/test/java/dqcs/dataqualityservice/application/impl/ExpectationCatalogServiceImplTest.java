package dqcs.dataqualityservice.application.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dqcs.dataqualityservice.api.dto.expectation.ExpectationCatalogDto;
import dqcs.dataqualityservice.infrastructure.entity.ExpectationCatalog;
import dqcs.dataqualityservice.infrastructure.repository.ExpectationCatalogRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExpectationCatalogServiceImpl Unit Tests")
class ExpectationCatalogServiceImplTest {

    @Mock
    private ExpectationCatalogRepository expectationCatalogRepository;

    @InjectMocks
    private ExpectationCatalogServiceImpl service;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnAllCatalogsAsDto() {
        UUID id = UUID.randomUUID();

        JsonNode kwargTypes = objectMapper.createObjectNode().put("threshold", "double");

        ExpectationCatalog catalog = mock(ExpectationCatalog.class);
        when(catalog.getId()).thenReturn(id);
        when(catalog.getCode()).thenReturn("non_null");
        when(catalog.getName()).thenReturn("Not Null");
        when(catalog.getDescription()).thenReturn("Checks nulls");
        when(catalog.getAllowedKwargTypes()).thenReturn(kwargTypes);
        when(catalog.getAllowedKwargs()).thenReturn(List.of("threshold"));
        when(catalog.isRequiresNumeric()).thenReturn(true);
        when(catalog.isSupportsRowCondition()).thenReturn(true);

        when(expectationCatalogRepository.findAll()).thenReturn(List.of(catalog));

        List<ExpectationCatalogDto> result = service.getAll();

        assertThat(result).hasSize(1);
        ExpectationCatalogDto dto = result.get(0);
        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.code()).isEqualTo("non_null");
        assertThat(dto.name()).isEqualTo("Not Null");
        assertThat(dto.description()).isEqualTo("Checks nulls");
        assertThat(dto.allowedArgs()).containsExactly("threshold");
        assertThat(dto.allowedKwargTypes()).isEqualTo(kwargTypes);
        assertThat(dto.requiresNumeric()).isTrue();
        assertThat(dto.supportsRawCondition()).isTrue();
    }
}