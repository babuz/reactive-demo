package com.learnreactive.reactivedemo.reactiveplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;

public class VirtualTimeTest {

    @Test
    public void withoutVirutalTime() {
        Flux<Long> fluxInteger = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.create(fluxInteger.log())
                .expectSubscription()
                .expectNext(0L, 1L, 2l)
                .verifyComplete();
    }

    @Test
    public void withVirutalTime() {
        VirtualTimeScheduler.getOrSet();

        Flux<Long> fluxInteger = Flux.interval(Duration.ofSeconds(1))
                .take(3);

        StepVerifier.withVirtualTime(() -> fluxInteger.log())
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(3))
                .expectNext(0L, 1L, 2L)
                .verifyComplete();
    }
}
