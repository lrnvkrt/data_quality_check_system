package dqcs.dataqualityservice.infrastructure.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "data_source")
public class DataSource {
    @Id
    private UUID id;

    private String name;

    private String description;

    @OneToMany(mappedBy = "dataSource", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Field> fields;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public DataSource(UUID id, String name, String description, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }

    public DataSource() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
