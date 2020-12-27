package com.learnreactive.reactivedemo.repository;

import com.learnreactive.reactivedemo.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@DataMongoTest
@DirtiesContext
public class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    List<Item> items = Arrays.asList(new Item(null, "Macbook", 100.99),
            new Item(null, "xbox", 300.99),
            new Item("12345", "playstation", 400.99));

    @Before
    public void setup() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(items))
                .flatMap(itemRepository::save)
                .doOnNext(item -> {
                    System.out.println(item);
                }).blockLast();
    }

    @Test
    public void db_findall_test() {
        StepVerifier.create(itemRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void db_findById_findOne() {
        StepVerifier.create(itemRepository.findById("12345").log())
                .expectSubscription()
                .expectNextMatches(item -> {
                    return item.getDescription().equals("playstation");
                });

    }

    @Test
    public void db_findById_findNone() {
        StepVerifier.create(itemRepository.findById("123456").log())
                .expectSubscription()
                .expectNextCount(0)
                .verifyComplete();

    }

    @Test
    public void db_findByDesription_findOne() {
        StepVerifier.create(itemRepository.findByDescription("xbox").log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void db_insert_item_successfully() {
        Item newItem = new Item(null, "Macbook Pro", 99.99);
        Mono<Item> savedItem = itemRepository.insert(newItem);

        StepVerifier.create(savedItem.log("Insert"))
                .expectSubscription()
                .expectNextMatches(item -> {
                    return item.getId() != null & item.getDescription().equals("Macbook Pro");
                })
                .verifyComplete();
    }

    @Test
    public void db_update_item_succesful() {
        Flux<Item> updatedItem = itemRepository.findByDescription("playstation")
                .map(item -> {
                    item.setPrice(1000.99);
                    return item;
                })
                .flatMap(item -> {
                    return itemRepository.save(item);
                });

        StepVerifier.create(updatedItem.log())
                .expectSubscription()
                .expectNextMatches(item -> {
                    return item.getId() != null && item.getPrice().equals(1000.99);
                })
                .verifyComplete();
    }

    @Test
    public void db_delete_item_by_id_success() {
        Mono<Void> voidFlux = itemRepository.deleteById("12345");

        StepVerifier.create(voidFlux.log("delete by id : void flux"))
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findById("12345").log("delete item by id"))
                .expectSubscription()
                .verifyComplete();

    }

    @Test
    public void db_delete_by_item_succesful() {
        Flux<Void> deleteItem = itemRepository.findByDescription("playstation")
                .flatMap(item -> {
                    return itemRepository.delete(item);
                });

        StepVerifier.create(deleteItem.log())
                .expectSubscription()
                .verifyComplete();

        StepVerifier.create(itemRepository.findByDescription("playstation"))
                .expectSubscription()
                .verifyComplete();
    }
}