package com.learnreactive.reactivedemo.controller;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class FluxControllerTest extends TestCase {

    @Autowired
    WebTestClient testClient;

    @Test
    public void Test_Approach_1_ReturnResult() {

        Flux<Integer> responseBody = testClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(responseBody.log())
                .expectSubscription()
                .expectNext(1, 2, 3, 4, 5)
                .verifyComplete();
    }

    @Test
    public void Test_Approach_2_VerifyBody() {
        testClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .hasSize(5);
    }

    @Test
    public void Test_Approach_3_EntityExchangeResult() {
        EntityExchangeResult<List<Integer>> listEntityExchangeResult = testClient.get().uri("/flux")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Integer.class)
                .returnResult();

        List<Integer> expectedListOfIntegers = Arrays.asList(1, 2, 3, 4, 5);

        assertEquals(expectedListOfIntegers, listEntityExchangeResult.getResponseBody());
    }

    @Test
    public void Test_Stream() {
        Flux<Long> longFlux = testClient.get()
                .uri("/fluxstream")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(longFlux.log())
                .expectNext(0L, 1L, 2L)
                .thenCancel()
                .verify();
    }


}