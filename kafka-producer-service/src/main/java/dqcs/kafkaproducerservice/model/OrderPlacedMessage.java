package dqcs.kafkaproducerservice.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderPlacedMessage {
    public UUID orderId;
    public UUID userId;
    public UUID productId;
    public int quantity;
    public BigDecimal totalAmount;
    public String currency;
    public Instant createdAt;

    public String country;
    public String city;
    public String postalCode;
    public String street;

    public String paymentMethod;
    public String deliveryType;

    public UUID correlationId;
    public String sourceSystem;

    public String email;
    public String ipAddress;
    public String ipv6Address;
    public String phoneNumber;
    public String passport;
    public String snils;
    public String createdDateStr;
    public String taxId10;
    public String taxId12;
}
