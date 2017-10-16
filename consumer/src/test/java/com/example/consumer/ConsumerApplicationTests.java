package com.example.consumer;

import com.example.consumer.domain.People;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.assertj.core.api.BDDAssertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerRule;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWireMock(port = 8081)
public class ConsumerApplicationTests {

    @Test
    public void test_should_return_all_persons() {
//        String json = "[{\"firstName\":\"Aniruth\",\"lastName\":\"Parthasarathy\"},{\"firstName\":\"Scott\",\"lastName\":\"Tiger\"}]";
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(Arrays.asList(new People("Aniruth", "Parthasarathy"), new People("Scott", "Tiger")));

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/persons"))
                .willReturn(WireMock.aResponse().withBody(json).withStatus(201)));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> persons = restTemplate.getForEntity("http://localhost:8081/persons",
                String.class);

        BDDAssertions.then(persons.getStatusCodeValue()).isEqualTo(201);
        BDDAssertions.then(persons.getBody()).isEqualTo(json);
    }

    @Rule
    public StubRunnerRule stubRunnerRule = new StubRunnerRule()
            .downloadStub("com.example", "producer")
            .workOffline(true)
            .withPort(8083);

    @Test
    public void test_should_return_all_persons_integration() {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(Arrays.asList(new People("Aniruth", "Parthasarathy"), new People("Scott", "Tiger")));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> persons = restTemplate.getForEntity("http://localhost:8083/persons",
                String.class);

        BDDAssertions.then(persons.getStatusCodeValue()).isEqualTo(201);
        BDDAssertions.then(persons.getBody()).isEqualTo(json);
    }

}
