package com.learnreactive.reactivedemo.config;

import com.learnreactive.reactivedemo.handler.ItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.learnreactive.reactivedemo.config.Constants.VII_GET_ALL_ITEMS_ENDPOINT;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ItemRouterFunction {

    @Bean
    public RouterFunction<ServerResponse> getRouterFunction(ItemHandler itemHandler) {
        return RouterFunctions.route(GET(VII_GET_ALL_ITEMS_ENDPOINT).and(accept(MediaType.APPLICATION_JSON)),
                itemHandler::getAllItems);

    }
}
