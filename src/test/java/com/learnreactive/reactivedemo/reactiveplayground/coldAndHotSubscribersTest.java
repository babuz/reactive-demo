package com.learnreactive.reactivedemo.reactiveplayground;

import org.junit.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import java.time.Duration;

public class coldAndHotSubscribersTest {

    @Test
    public void coldPublishersTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        stringFlux.subscribe(s -> System.out.println("subscriber 1 : value : " + s));
        Thread.sleep(2000);
        stringFlux.subscribe(s -> System.out.println("subscriber 2 : value : " + s));
        Thread.sleep(3000);
    }

    @Test
    public void hotPublishersTest() throws InterruptedException {
        Flux<String> stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
                .delayElements(Duration.ofSeconds(1));

        ConnectableFlux<String> connectableFlux = stringFlux.publish();
        connectableFlux.connect();

        connectableFlux.subscribe(s -> System.out.println("subscriber 1 : value : " + s));
        Thread.sleep(2000);

        connectableFlux.subscribe(s -> System.out.println("subscriber 2 : value : " + s));
        Thread.sleep(2000);
    }
}
