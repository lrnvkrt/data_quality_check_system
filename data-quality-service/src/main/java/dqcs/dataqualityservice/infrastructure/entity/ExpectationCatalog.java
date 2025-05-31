package dqcs.dataqualityservice.infrastructure.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "expectation_catalog")
public class ExpectationCatalog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String code;

    private String name;

    private String description;

    @Column(name = "allowed_kwargs", columnDefinition = "text[]")
    @SuppressWarnings("JpaAttributeTypeInspection")
    private List<String> allowedKwargs;

    @Column(name = "default_kwargs", columnDefinition = "jsonb")
    private String defaultKwargs;


    @Column(name = "allowed_kwarg_types", columnDefinition = "jsonb")
    @Type(JsonType.class)
    private JsonNode allowedKwargTypes;

    @Column(name = "requires_numeric")
    private boolean requiresNumeric;

    @Column(name = "supports_row_condition")
    private boolean supportsRowCondition;

    public ExpectationCatalog(UUID id, String code, String name, String description, List<String> allowedKwargs, String defaultKwargs, boolean requiresNumeric, boolean supportsRowCondition) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.allowedKwargs = allowedKwargs;
        this.defaultKwargs = defaultKwargs;
        this.requiresNumeric = requiresNumeric;
        this.supportsRowCondition = supportsRowCondition;
    }

    public ExpectationCatalog() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAllowedKwargs() {
        return allowedKwargs;
    }

    public void setAllowedKwargs(List<String> allowedKwargs) {
        this.allowedKwargs = allowedKwargs;
    }

    public String getDefaultKwargs() {
        return defaultKwargs;
    }

    public void setDefaultKwargs(String defaultKwargs) {
        this.defaultKwargs = defaultKwargs;
    }

    public boolean isRequiresNumeric() {
        return requiresNumeric;
    }

    public void setRequiresNumeric(boolean requiresNumeric) {
        this.requiresNumeric = requiresNumeric;
    }

    public boolean isSupportsRowCondition() {
        return supportsRowCondition;
    }

    public void setSupportsRowCondition(boolean supportsRowCondition) {
        this.supportsRowCondition = supportsRowCondition;
    }

    public JsonNode getAllowedKwargTypes() {
        return allowedKwargTypes;
    }
}

