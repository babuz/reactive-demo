package com.learnreactive.reactivedemo;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class JoinFluxItemsTest {

    List<String> first = Arrays.asList("A","B","C");
    List<String> second = Arrays.asList("D","E");
    Flux<String> firstFlux = Flux.fromIterable(first).delayElements(Duration.ofSeconds(1));
    Flux<String> secondFlux = Flux.fromIterable(second).delayElements(Duration.ofSeconds(1));

    @Test
    public void joinWith_Concat_WithDelay(){
        Flux<String> joinedFlux = firstFlux.concatWith(secondFlux).log();

        StepVerifier.create(joinedFlux)
                .expectNext("A","B","C","D","E")
                .verifyComplete();
    }

    @Test
    public void joinWith_Merge_WithDelay(){
        Flux<String> joinedFlux = firstFlux.mergeWith(secondFlux).log();

        StepVerifier.create(joinedFlux)
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void joinWith_Zip_WithDelay(){
        Flux<String> joinedFlux = Flux.zip(firstFlux,secondFlux,(f,s)->{
            System.out.println(f + s);
            return f.toLowerCase() + s.toLowerCase();
        } ).log();

        StepVerifier.create(joinedFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

}
