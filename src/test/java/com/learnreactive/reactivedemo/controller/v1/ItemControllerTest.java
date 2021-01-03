package com.learnreactive.reactivedemo.controller.v1;

import com.learnreactive.reactivedemo.document.Item;
import com.learnreactive.reactivedemo.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static com.learnreactive.reactivedemo.config.Constants.VI_GET_ALL_ITEMS_ENDPOINT;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    WebTestClient webTestClient;

    private List<Item> getItems() {
        return Arrays.asList(
                new Item("12345", "playstation", 199.99),
                new Item("67890", "Macbook", 299.99),
                new Item("23456", "xbox", 399.99),
                new Item("78901", "Dell Inspiron", 499.99),
                new Item("34567", "Hp power", 599.99)
        );
    }

    @Before
    public void setup() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(getItems()))
                .flatMap(itemRepository::save)
                .doOnNext(item -> {
                    System.out.println("created test item : " + item);
                })
                .blockLast();
    }

    @Test
    public void testFindAllEndpoint() {
        webTestClient.get().uri(VI_GET_ALL_ITEMS_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(5);
    }

    @Test
    public void testGetOneItemById() {
        webTestClient.get().uri(VI_GET_ALL_ITEMS_ENDPOINT + "/{d}", 12345)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.description").isEqualTo("playstation")
                .jsonPath("$.price").isEqualTo(199.99);
    }

    @Test
    public void testGetOneItemById_notfound() {
        webTestClient.get().uri(VI_GET_ALL_ITEMS_ENDPOINT + "/{d}", 00000)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void testDeleteItemSuccessfully() {
        webTestClient.delete().uri(VI_GET_ALL_ITEMS_ENDPOINT + "/{id}", 12345)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .isEmpty();
    }

    @Test
    public void testDeleteItemNotFound() {
        webTestClient.delete().uri(VI_GET_ALL_ITEMS_ENDPOINT + "/{id}", 0000)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItem_Success() {

        Item item = Item.builder()
                .id(null)
                .description("LG TV 201 Inch")
                .price(399.99)
                .build();

        webTestClient.post().uri(VI_GET_ALL_ITEMS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("LG TV 201 Inch");
    }

    @Test
    public void updateItem_Success() {
        Item item = Item.builder()
                .id("12345")
                .description("PlayStation V5.0")
                .price(599.99)
                .build();

        webTestClient.put().uri(VI_GET_ALL_ITEMS_ENDPOINT + "/{id}", 12345)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("12345")
                .jsonPath("$.description").isEqualTo("PlayStation V5.0")
                .jsonPath("$.price").isEqualTo(599.99);
    }

    @Test
    public void updateItem_unknowItem() {

        Item item = Item.builder()
                .id("12345")
                .description("PlayStation V5.0")
                .price(599.99)
                .build();

        webTestClient.put().uri(VI_GET_ALL_ITEMS_ENDPOINT + "/{id}", 0000)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound();
    }
}