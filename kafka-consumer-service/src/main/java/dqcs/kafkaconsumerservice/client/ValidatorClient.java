package dqcs.kafkaconsumerservice.client;

import dqcs.kafkaconsumerservice.messaging.dto.GenericEvent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "batch.client",
        url = "${app.validator.base-url}"
)
public interface ValidatorClient {

    @PostMapping("/batch/{topic}")
    void sendBatch(@PathVariable String topic, @RequestBody List<GenericEvent> events);
}
