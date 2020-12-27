package com.learnreactive.reactivedemo.config;

import com.learnreactive.reactivedemo.handler.SampleRestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> getRouterConfig(SampleRestHandler sampleRestHandler) {
        return RouterFunctions.route(GET("/functional/flux")
                .and(accept(MediaType.APPLICATION_JSON_UTF8)), sampleRestHandler::fluxhandler);
    }
}
