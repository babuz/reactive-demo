package com.learnreactive.reactivedemo.reactiveplayground;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReactiveDemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void FluxTest_WithoutError() {
        Flux<String> fluxString = Flux.just("Spring", "Spring1", "Spring 2")
                .log();
        StepVerifier.create(fluxString)
                .expectNext("Spring")
                .expectNext("Spring1")
                .expectNext("Spring 2")
                .verifyComplete();
    }

    @Test
    public void FluxTest_WithError() {
        Flux<String> fluxString = Flux.just("Spring", "Spring1", "Spring 2")
                .concatWith(Flux.error(new RuntimeException("Flux throwing RuntimeException")))
                .log();

        StepVerifier.create(fluxString)
                .expectNext("Spring")
                .expectNext("Spring1")
                .expectNext("Spring 2")
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    public void MonoTest() {
        Mono<String> mono = Mono.just("test").log();
        StepVerifier.create(mono)
                .expectNext("test")
                .verifyComplete();

        Mono<String> monoWithError = Mono.error(new RuntimeException("test error"));
        StepVerifier.create(monoWithError.log())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void FluxTransformationTest() {
        List<String> listOfNames = Arrays.asList("Ganesh", "Babu", "Archu", "Pappili");
        Flux<String> flux = Flux.fromIterable(listOfNames);
        flux = flux.filter(s -> s.length() > 5)
                .map(s -> s.toUpperCase())
                .log();

        StepVerifier.create(flux)
                .expectNext("GANESH","PAPPILI")
                .verifyComplete();
    }
}
