package com.example.producer.api;

import com.example.producer.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@EnableBinding(Source.class)
public class PersonController {

    private List<Person> personList = new ArrayList<>();

    @Autowired
    public PersonController() {
        if (CollectionUtils.isEmpty(this.personList)) {
            personList = Arrays.asList(
                    new Person(100, "Aniruth", "Parthasarathy"),
                    new Person(101, "Scott", "Tiger"));
        }
    }

    @GetMapping(value = "/persons", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<Person>> persons() {
        return ResponseEntity.status(201).body(personList);
    }

    @Autowired
    private Source source;

    @PostMapping("/message")
    void message() {
        source.output().send(MessageBuilder.withPayload(
                new Person(999, "From", "Producer")
        ).build());
    }
}
