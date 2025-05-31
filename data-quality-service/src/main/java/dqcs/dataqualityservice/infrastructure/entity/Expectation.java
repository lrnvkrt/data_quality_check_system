package dqcs.dataqualityservice.infrastructure.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "expectation")
public class Expectation {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expectation_type_id", referencedColumnName = "id", nullable = false)
    private ExpectationCatalog expectationType;

    @Column(columnDefinition = "jsonb")
    @Type(JsonType.class)
    private String kwargs;

    @Column(name = "row_condition")
    private String rowCondition;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    private String description;

    private boolean enabled;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    public Expectation(UUID id, Field field, ExpectationCatalog expectationType, String kwargs, String rowCondition, Severity severity, String description, boolean enabled, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.field = field;
        this.expectationType = expectationType;
        this.kwargs = kwargs;
        this.rowCondition = rowCondition;
        this.severity = severity;
        this.description = description;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public Expectation() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public ExpectationCatalog getExpectationType() {
        return expectationType;
    }

    public void setExpectationType(ExpectationCatalog expectationType) {
        this.expectationType = expectationType;
    }

    public String getKwargs() {
        return kwargs;
    }

    public void setKwargs(String kwargs) {
        this.kwargs = kwargs;
    }

    public String getRowCondition() {
        return rowCondition;
    }

    public void setRowCondition(String rowCondition) {
        this.rowCondition = rowCondition;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}

