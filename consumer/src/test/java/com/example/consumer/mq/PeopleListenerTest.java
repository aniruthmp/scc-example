package com.example.consumer.mq;

import com.example.consumer.domain.People;
import org.assertj.core.api.BDDAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubTrigger;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureStubRunner(workOffline = true, ids = "com.example:producer")
public class PeopleListenerTest {

    @Autowired
    private PeopleListener peopleListener;

//    @Autowired
//    private Sink sink;
//
//    @Test
//    public void test_should_consume_people_message() throws Exception {
//        String fName = "From";
//        String lName = "Queue";
//        String fullName = fName + " " + lName;
//        Message<People> message = MessageBuilder.withPayload(
//                new People("From", "Queue")
//        ).build();
//        SubscribableChannel input = sink.input();
//        input.send(message);
//
//        BDDAssertions.then(this.peopleListener.fullName).isEqualTo(fullName);
//    }

    @Test
    public void test_should_consume_people_message() throws Exception {
        String fName = "From";
        String lName = "Producer";
        String fullName = fName + " " + lName;
        stubTrigger.trigger("triggerPerson");
        BDDAssertions.then(this.peopleListener.fullName).isEqualTo(fullName);
    }

    @Autowired
    private StubTrigger stubTrigger;
}