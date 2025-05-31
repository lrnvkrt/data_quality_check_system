package dqcs.kafkaproducerservice.service;

import dqcs.kafkaproducerservice.model.OrderPlacedMessage;
import dqcs.kafkaproducerservice.model.PaymentTransactionMessage;
import dqcs.kafkaproducerservice.model.ProductCatalogUpdatedMessage;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Service
public class KafkaProducerService {

    private final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Faker faker = new Faker();

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRateString = "${app.kafka.message-send-interval-ms}")
    public void sendRandomMessages() {
        log.info("Sending faker-generated messages");
        kafkaTemplate.send("ecom.order.placed", generateOrderPlaced());
        kafkaTemplate.send("ecom.product.catalog.updated", generateProductUpdate());
        kafkaTemplate.send("ecom.payment.transaction", generatePaymentTransaction());
    }

    private OrderPlacedMessage generateOrderPlaced() {
        OrderPlacedMessage msg = new OrderPlacedMessage();

        msg.orderId = maybe(UUID::randomUUID);
        msg.userId = maybe(UUID::randomUUID);
        msg.productId = maybe(UUID::randomUUID);
        msg.quantity = faker.number().numberBetween(-100, 1000);
        msg.totalAmount = maybe(() -> BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000)));
        msg.currency = maybe(() -> faker.currency().code());
        msg.createdAt = maybe(Instant::now);

        msg.country = maybe(() -> faker.address().country());
        msg.city = maybe(() -> faker.address().city());
        msg.postalCode = maybe(() -> faker.address().zipCode());
        msg.street = maybe(() -> faker.address().streetAddress());

        msg.paymentMethod = maybe(() -> faker.options().option("CARD", "CASH", "APPLE_PAY", "INVALID", ""));
        msg.deliveryType = maybe(() -> faker.options().option("COURIER", "PICKUP", "TELEPORT"));

        msg.correlationId = maybe(UUID::randomUUID);
        msg.sourceSystem = maybe(() -> faker.options().option("web", "mobile", "kiosk", "unknown"));

        msg.email = maybe(() -> maybeInvalid(
                () -> faker.internet().emailAddress(),
                () -> faker.lorem().word()
        ));

        msg.ipAddress = maybe(() -> maybeInvalid(
                () -> faker.internet().ipV4Address(),
                () -> faker.lorem().characters(8)
        ));

        msg.ipv6Address = maybe(() -> maybeInvalid(
                () -> faker.internet().ipV6Address(),
                () -> faker.regexify("dead::beef::broken")
        ));

        msg.phoneNumber = maybe(() -> maybeInvalid(
                () -> faker.regexify("\\+7\\d{10}"),
                () -> faker.regexify("12345")
        ));

        msg.passport = maybe(() -> maybeInvalid(
                () -> faker.regexify("\\d{4}\\s\\d{6}"),
                () -> faker.regexify("[A-Z]{4}\\d{6}")
        ));

        msg.snils = maybe(() -> maybeInvalid(
                () -> faker.regexify("\\d{3}-\\d{3}-\\d{3}\\s\\d{2}"),
                () -> faker.regexify("\\d{9}")
        ));

        msg.createdDateStr = maybe(() -> maybeInvalid(
                () -> "2024-12-31",
                () -> "31.12.2024"
        ));

        msg.taxId10 = maybe(() -> maybeInvalid(
                () -> faker.regexify("\\d{10}"),
                () -> faker.regexify("\\d{8}")
        ));

        msg.taxId12 = maybe(() -> maybeInvalid(
                () -> faker.regexify("\\d{12}"),
                () -> faker.regexify("INVALID")
        ));

        return msg;
    }

    private ProductCatalogUpdatedMessage generateProductUpdate() {
        ProductCatalogUpdatedMessage msg = new ProductCatalogUpdatedMessage();

        msg.eventId = maybe(UUID::randomUUID);
        msg.productId = maybe(UUID::randomUUID);
        msg.eventType = maybe(() -> faker.options().option("CREATED", "UPDATED", "DELETED", "BROKEN"));

        msg.timestamp = maybe(Instant::now);
        msg.name = maybe(() -> faker.commerce().productName());

        msg.description = maybe(() -> faker.lorem().paragraph());
        msg.price = maybe(() -> faker.bool().bool()
                ? BigDecimal.valueOf(-faker.number().randomDouble(2, 1, 500))
                : BigDecimal.valueOf(faker.number().randomDouble(2, 1, 999)));

        msg.currency = maybe(() -> faker.currency().code());
        msg.available = faker.bool().bool();

        msg.category = maybe(() -> maybeInvalid(
                () -> faker.commerce().department(),
                () -> "INVALID_CATEGORY"
        ));

        msg.updatedBy = maybe(UUID::randomUUID);
        msg.correlationId = maybe(UUID::randomUUID);

        msg.ipAddress = maybe(() -> maybeInvalid(
                () -> faker.internet().ipV4Address(),
                () -> "not.an.ip"
        ));

        msg.ipv6Address = maybe(() -> maybeInvalid(
                () -> faker.internet().ipV6Address(),
                () -> "::::"
        ));

        msg.supportEmail = maybe(() -> maybeInvalid(
                () -> faker.internet().emailAddress(),
                () -> faker.lorem().word()
        ));

        msg.skuCode = maybe(() -> maybeInvalid(
                () -> faker.regexify("SKU-\\d{5}"),
                () -> faker.regexify("SKU_ABC")
        ));

        msg.shortDescription = maybe(() -> faker.lorem().characters(faker.number().numberBetween(0, 200)));

        msg.createdDateStr = maybe(() -> maybeInvalid(
                () -> "2024-11-15",
                () -> "15-11-2024"
        ));

        return msg;
    }

    private PaymentTransactionMessage generatePaymentTransaction() {
        PaymentTransactionMessage msg = new PaymentTransactionMessage();

        msg.transactionId = maybe(UUID::randomUUID);
        msg.orderId = maybe(UUID::randomUUID);
        msg.userId = maybe(UUID::randomUUID);

        msg.amount = maybe(() -> faker.bool().bool()
                ? new BigDecimal(faker.number().numberBetween(-100, 0))
                : BigDecimal.valueOf(faker.number().randomDouble(2, 10, 500)));

        msg.currency = maybe(() -> faker.currency().code());

        msg.paymentMethod = maybe(() -> maybeInvalid(
                () -> faker.business().creditCardType(),
                () -> faker.lorem().word()
        ));

        msg.status = maybe(() -> faker.options().option("SUCCESS", "FAILED", "PENDING", "UNKNOWN_STATUS", ""));

        msg.timestamp = maybe(Instant::now);

        msg.providerTransactionId = maybe(() -> faker.regexify("[A-Z0-9]{10,16}"));

        msg.failureReason = "FAILED".equals(msg.status)
                ? maybe(() -> maybeInvalid(
                () -> faker.lorem().sentence(),
                () -> ""
        ))
                : null;

        msg.correlationId = maybe(UUID::randomUUID);
        msg.sourceSystem = maybe(() -> faker.options().option("mobile-app", "web-app", "internal-service", "unknown"));


        msg.bankCard = maybe(() -> maybeInvalid(
                () -> faker.regexify("\\d{16}"),
                () -> faker.regexify("1234-5678-XXXX")
        ));

        msg.email = maybe(() -> maybeInvalid(
                () -> faker.internet().emailAddress(),
                () -> faker.lorem().word()
        ));

        msg.createdDateStr = maybe(() -> maybeInvalid(
                () -> "2024-12-31",
                () -> "31.12.2024"
        ));

        msg.comment = maybe(() -> faker.lorem().characters(faker.number().numberBetween(0, 200)));

        return msg;
    }

    private static <T> T maybe(Supplier<T> supplier) {
        return ThreadLocalRandom.current().nextBoolean() ? null : supplier.get();
    }

    private static String maybeInvalid(Supplier<String> validSupplier, Supplier<String> invalidSupplier) {
        return ThreadLocalRandom.current().nextBoolean()
                ? validSupplier.get()
                : invalidSupplier.get();
    }
}


