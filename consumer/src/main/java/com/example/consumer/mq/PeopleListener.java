package com.example.consumer.mq;

import com.example.consumer.model.People;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PeopleListener {

    String fullName;

    @StreamListener(Sink.INPUT)
    public void onMessage(People people) {
        log.info("Received Message: {}", people.toString());
        this.fullName = people.getFirstName() + " " + people.getLastName();
    }
}
