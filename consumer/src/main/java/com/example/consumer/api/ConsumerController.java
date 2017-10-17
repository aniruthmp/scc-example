package com.example.consumer.api;

import com.example.consumer.domain.Consumer;
import com.example.consumer.model.People;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
public class ConsumerController {
    private final RestTemplate restTemplate;
    int producerAppPort = 8082;

    public ConsumerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setProducerAppPort(int producerAppPort) {
        this.producerAppPort = producerAppPort;
    }

    @GetMapping(value = "/consumers", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Consumer>> people() {
        log.info("Came inside /consumers GET API");
        ResponseEntity<String> response = this.restTemplate.getForEntity(
                URI.create("http://localhost:" + producerAppPort + "/persons"),
                String.class);
        log.info("Response: {}", response);
        String responseBody = response.getBody();
        if (Objects.nonNull(responseBody)) {
            Gson gson = new GsonBuilder().create();
            Type token = new TypeToken<Collection<People>>() {}.getType();
            List<People> personList = gson.fromJson(responseBody, token);
            List<Consumer> consumers = new ArrayList<>();
            personList.parallelStream().forEach(people -> {
                consumers.add(new Consumer(people.getPersonId(), people.getFirstName(), people.getLastName()));
            });
            log.info("Returning {} with Payload {}", HttpStatus.ACCEPTED, consumers);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(consumers);
        } else {
            log.warn("Returning {} with Empty Payload", HttpStatus.ACCEPTED);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }
    }
}
