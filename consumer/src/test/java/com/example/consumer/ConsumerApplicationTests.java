package com.example.consumer;

import com.example.consumer.api.PeopleController;
import com.example.consumer.domain.People;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureWireMock(port = 8081)
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext
@AutoConfigureStubRunner(workOffline = true, ids = "com.example:producer:+:stubs")
public class ConsumerApplicationTests {

    private List<People> personList = new ArrayList<>();
    private String json;
    @Value("${stubrunner.runningstubs.producer.port}")
    int producerPort;

    @Autowired
    PeopleController peopleController;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void setup() {
        peopleController.setPERSON_APP_PORT(producerPort);
        if (CollectionUtils.isEmpty(this.personList)) {
            personList = Arrays.asList(
                    new People(100, "Aniruth", "Parthasarathy"),
                    new People(101, "Scott", "Tiger"));
            Gson gson = new GsonBuilder().create();
            json = gson.toJson(personList);
        }
    }

    @Test
    public void test_should_return_all_people_withMock() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/people"))
                .willReturn(WireMock.aResponse().withBody(json).withStatus(HttpStatus.ACCEPTED.value())));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> persons = restTemplate.getForEntity("http://localhost:8081/people",
                String.class);

        BDDAssertions.then(persons.getStatusCodeValue()).isEqualTo(HttpStatus.ACCEPTED.value());
        BDDAssertions.then(persons.getBody()).isEqualTo(json);
    }

    @Test
    public void test_should_return_all_people_withStub() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/people").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isAccepted())
                .andExpect(content().string(json));
    }

    @Test
    public void test_should_return_all_persons_integration() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> persons = restTemplate.getForEntity(
                "http://localhost:" + producerPort + "/persons", String.class);

        String strPersons = "[{\"personId\":100,\"firstName\":\"Aniruth\",\"lastName\":\"Parthasarathy\"}," +
                "{\"personId\":101,\"firstName\":\"Scott\",\"lastName\":\"Tiger\"}]";

        BDDAssertions.then(persons.getStatusCodeValue()).isEqualTo(201);
        BDDAssertions.then(persons.getBody()).isEqualTo(strPersons);
    }

}
