package com.learnreactive.reactivedemo.handler;

import com.learnreactive.reactivedemo.document.Item;
import com.learnreactive.reactivedemo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class ItemHandler {

    @Autowired
    private ItemRepository itemRepository;

    public Mono<ServerResponse> getAllItems(ServerRequest request) {
        return ServerResponse.ok().body(itemRepository.findAll(), Item.class);
    }

    public Mono<ServerResponse> getOneItemById(ServerRequest request) {
        String id = request.pathVariable("id");

        if (StringUtils.isEmpty(id)) {
            return ServerResponse.badRequest().build();
        }

        return itemRepository.findById(id)
                .flatMap(item -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(fromObject(item)))
                .switchIfEmpty(ServerResponse.notFound().build());

    }
}
