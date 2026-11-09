package com.ticketflow.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

// Le nom "event-service" correspond au spring.application.name de l'autre service (Eureka le trouvera tout seul)
@FeignClient(name = "event-service")
public interface EventClient {

    // On reprend EXACTEMENT le même chemin que dans EventController
    @GetMapping("/api/events/{id}")
    Map<String, Object> getEventById(@PathVariable("id") Long id);
}