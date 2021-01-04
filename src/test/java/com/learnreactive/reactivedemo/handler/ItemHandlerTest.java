package com.learnreactive.reactivedemo.handler;

import com.learnreactive.reactivedemo.document.Item;
import com.learnreactive.reactivedemo.repository.ItemRepository;
import io.netty.util.internal.StringUtil;
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
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static com.learnreactive.reactivedemo.config.Constants.VII_GET_ALL_ITEMS_ENDPOINT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
@DirtiesContext
@ActiveProfiles("test")
public class ItemHandlerTest {

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
        webTestClient.get().uri(VII_GET_ALL_ITEMS_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(5);
    }

    @Test
    public void testFindAll_anotherApproach() {
        EntityExchangeResult<List<Item>> itemsResult = webTestClient.get().uri(VII_GET_ALL_ITEMS_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .returnResult();

        List<Item> expectedListofItems = Arrays.asList(
                new Item("12345", "playstation", 199.99),
                new Item("67890", "Macbook", 299.99),
                new Item("23456", "xbox", 399.99),
                new Item("78901", "Dell Inspiron", 499.99),
                new Item("34567", "Hp power", 599.99)
        );

        assertTrue(itemsResult.getResponseBody().stream()
                .allMatch(item -> {
                    return expectedListofItems.contains(item);
                }));
    }

    @Test
    public void testFindAll_Validate_Size_Approach() {
        List<Item> listOfItems = webTestClient.get().uri(VII_GET_ALL_ITEMS_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .returnResult()
                .getResponseBody();

        assertEquals(5, listOfItems.size());
    }

    @Test
    public void testFindAll_getIdApproach() {
        EntityExchangeResult<List<Item>> listEntityExchangeResult = webTestClient.get().uri(VII_GET_ALL_ITEMS_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .returnResult();

        assertTrue(listEntityExchangeResult.getResponseBody().stream()
                .allMatch(item -> !StringUtil.isNullOrEmpty(item.getId())));
    }

}