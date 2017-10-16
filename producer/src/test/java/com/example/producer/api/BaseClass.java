package com.example.producer.api;

import com.example.producer.ProducerApplication;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ProducerApplication.class)
@AutoConfigureMessageVerifier
public class BaseClass {

    @Autowired
    PersonController personController;

    @Before
    public void setup() {
        RestAssuredMockMvc.standaloneSetup(personController);
    }

    public void triggerMessage() {
        personController.message();
    }
}
