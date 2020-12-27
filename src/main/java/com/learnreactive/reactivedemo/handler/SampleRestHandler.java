package com.learnreactive.reactivedemo.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class SampleRestHandler {

    public Mono<ServerResponse> fluxhandler(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(Flux.just(1, 2, 3, 4, 5), Integer.class);
    }
}
