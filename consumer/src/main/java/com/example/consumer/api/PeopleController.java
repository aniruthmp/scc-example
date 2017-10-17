package com.example.consumer.api;

import com.example.consumer.domain.People;
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
public class PeopleController {
    private final RestTemplate restTemplate;
    int PERSON_APP_PORT = 8082;

    public PeopleController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setPERSON_APP_PORT(int PERSON_APP_PORT) {
        this.PERSON_APP_PORT = PERSON_APP_PORT;
    }

    @GetMapping(value = "/people", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<People>> people() {
        log.info("Came inside /people GET API");
        ResponseEntity<String> response = this.restTemplate.getForEntity(
                URI.create("http://localhost:" + PERSON_APP_PORT + "/persons"),
                String.class);
        log.info("Response: {}", response);
        String responseBody = response.getBody();
        if (Objects.nonNull(responseBody)) {
            Gson gson = new GsonBuilder().create();
            Type token = new TypeToken<Collection<People>>() {}.getType();
            List<People> personList = gson.fromJson(responseBody, token);
            personList.parallelStream().forEach(people -> {
                people.setFullName();
            });
            log.info("Returning {} with Payload {}", HttpStatus.ACCEPTED, personList);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(personList);
        } else {
            log.warn("Returning {} with Empty Payload", HttpStatus.ACCEPTED);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>());
        }
    }
}
