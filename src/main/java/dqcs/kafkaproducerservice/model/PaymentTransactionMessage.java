package dqcs.kafkaproducerservice.model;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class PaymentTransactionMessage {
    public UUID transactionId;
    public UUID orderId;
    public UUID userId;

    public BigDecimal amount;
    public String currency;
    public String paymentMethod;
    public String status;
    public Instant timestamp;

    public String providerTransactionId;
    public String failureReason;

    public UUID correlationId;
    public String sourceSystem;

    public String bankCard;
    public String email;
    public String createdDateStr;
    public String comment;
}
