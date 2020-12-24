package com.learnreactive.reactivedemo.reactiveplayground;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class BackPressureTest {

    @Test
    public void backPressureTestRange() {
        Flux<Integer> finiteIntegers = Flux.range(10, 20);

        StepVerifier.create(finiteIntegers.log())
                .expectSubscription()
                .thenRequest(1)
                .expectNext(10)
                .thenRequest(2)
                .expectNext(11, 12)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure_subscriber() {
        Flux<Integer> finiteIntegers = Flux.range(-1, 10);

        finiteIntegers.subscribe(value -> System.out.println("value : " + value),
                (e) -> System.out.println("error occured " + e),
                () -> System.out.println("all items completed"),
                (sub) -> sub.request(13));

    }

    @Test
    public void backPressure_customized() {
        Flux<Integer> finiteIntegers = Flux.range(1, 10).log();

        finiteIntegers.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                System.out.println("value = " + value);
                if (value == 5)
                    cancel();
            }
        });
    }
}