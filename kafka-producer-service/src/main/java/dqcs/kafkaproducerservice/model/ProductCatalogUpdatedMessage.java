package dqcs.kafkaproducerservice.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ProductCatalogUpdatedMessage {
    public UUID eventId;
    public UUID productId;
    public String eventType;
    public Instant timestamp;

    public String name;
    public String description;
    public BigDecimal price;
    public String currency;
    public boolean available;
    public String category;

    public UUID updatedBy;
    public UUID correlationId;

    public String ipAddress;
    public String ipv6Address;
    public String supportEmail;
    public String skuCode;
    public String createdDateStr;
    public String shortDescription;
}