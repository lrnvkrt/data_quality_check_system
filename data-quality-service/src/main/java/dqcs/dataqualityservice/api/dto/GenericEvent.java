package dqcs.dataqualityservice.api.dto;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Событие, содержащее полезную нагрузку для валидации")
public class GenericEvent{
    @Schema(
            description = "Имя топика или источника, откуда пришло событие (может дублироваться с path-параметром)",
            example = "orders.topic"
    )
    private String topic;

    @Schema(
            description = """
            Полезная нагрузка события в формате JSON.
            Структура зависит от источника данных и правил валидации.
            """,
            type = "object",
            example = """
            {
              "order_id": "A123",
              "region": "EU",
              "amount": 42.0
            }
            """
    )
    private JsonNode payload;

    public GenericEvent(String topic, JsonNode payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }
}