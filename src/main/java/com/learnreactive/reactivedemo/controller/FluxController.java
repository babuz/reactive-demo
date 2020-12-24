package com.learnreactive.reactivedemo.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class FluxController {

    @GetMapping("/flux")
    public Flux<Integer> getNames() {
        return Flux.just(1, 2, 3, 4, 5)
                .delayElements(Duration.ofMillis(500));
    }

    @GetMapping(value = "/fluxstream", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Integer> getNamesByStream() {
        return Flux.just(1, 2, 3, 4, 5)
                .delayElements(Duration.ofMillis(500));
    }
}
