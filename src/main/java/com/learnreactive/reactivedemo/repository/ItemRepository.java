package com.learnreactive.reactivedemo.repository;

import com.learnreactive.reactivedemo.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveMongoRepository<Item, String> {

    Flux<Item> findByDescription(final String description);
}
