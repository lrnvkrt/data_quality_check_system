package dqcs.kafkaconsumerservice.sender.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dqcs.kafkaconsumerservice.client.ValidatorClient;
import dqcs.kafkaconsumerservice.messaging.dto.GenericEvent;
import dqcs.kafkaconsumerservice.messaging.registry.TopicBufferRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AsyncBatchSender Unit Tests")
class AsyncBatchSenderTest {

    @Mock
    private TopicBufferRegistry bufferRegistry;

    @Mock
    private ValidatorClient validatorClient;

    @InjectMocks
    private AsyncBatchSender asyncBatchSender;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private GenericEvent createEvent(String topic, String payloadJson) throws Exception {
        JsonNode payload = objectMapper.readTree(payloadJson);
        return new GenericEvent(topic, payload);
    }

    @Nested
    @DisplayName("sendBatch()")
    class SendBatchTests {

        @Test
        @DisplayName("should send batch successfully via validatorClient")
        void shouldSendBatchSuccessfully() throws Exception {
            String topic = "test-topic";
            List<GenericEvent> batch = List.of(
                    createEvent(topic, "{\"key\":\"value1\"}"),
                    createEvent(topic, "{\"key\":\"value2\"}")
            );
            asyncBatchSender.sendBatch(topic, batch);
            verify(validatorClient).sendBatch(topic, batch);
        }

        @Test
        @DisplayName("should catch and log exception if sending fails")
        void shouldCatchExceptionWhenSendingFails() throws Exception {
            String topic = "test-topic";
            List<GenericEvent> batch = List.of(createEvent(topic, "{\"key\":\"error\"}"));
            doThrow(new RuntimeException("Validation error"))
                    .when(validatorClient).sendBatch(anyString(), anyList());
            assertThatCode(() -> asyncBatchSender.sendBatch(topic, batch))
                    .doesNotThrowAnyException();
            verify(validatorClient).sendBatch(topic, batch);
        }
    }

    @Nested
    @DisplayName("flush()")
    class FlushTests {

        private final String topic = "test-topic";

        @BeforeEach
        void setupRegistryMock() {
            when(bufferRegistry.allTopics()).thenReturn(Set.of(topic));
            when(bufferRegistry.getBatchSize(topic)).thenReturn(10);
        }

        @Test
        @DisplayName("should send batch if queue contains elements")
        void shouldSendBatchFromQueue() throws Exception {
            GenericEvent event = createEvent(topic, "{\"data\":\"test\"}");
            BlockingQueue<GenericEvent> queue = new LinkedBlockingQueue<>();
            queue.add(event);
            when(bufferRegistry.getQueue(topic)).thenReturn(queue);
            asyncBatchSender.flush();
            verify(validatorClient).sendBatch(eq(topic), argThat(list -> list.contains(event)));
        }

        @Test
        @DisplayName("should skip sending if queue is empty")
        void shouldSkipIfQueueIsEmpty() {
            BlockingQueue<GenericEvent> emptyQueue = new LinkedBlockingQueue<>();
            when(bufferRegistry.getQueue(topic)).thenReturn(emptyQueue);
            asyncBatchSender.flush();
            verify(validatorClient, never()).sendBatch(any(), any());
        }
    }
}