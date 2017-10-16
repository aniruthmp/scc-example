package com.example.producer.api;

import com.example.producer.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@EnableBinding(Source.class)
public class PersonController {

    @GetMapping("/persons")
    ResponseEntity<List<Person>> persons() {
        return ResponseEntity.status(201).body(Arrays.asList(new Person("Aniruth", "Parthasarathy"),
                new Person("Scott", "Tiger")));
    }

    @Autowired
    private Source source;

    @PostMapping("/message")
    void message() {
        source.output().send(MessageBuilder.withPayload(
                new Person("From", "Producer")
        ).build());
    }
}
