package com.learnreactive.reactivedemo.config;

import com.learnreactive.reactivedemo.document.Item;
import com.learnreactive.reactivedemo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("!test")
public class InitializeData implements CommandLineRunner {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {
        setInitialData();
    }

    private List<Item> getItems() {
        return Arrays.asList(
                new Item("12345", "playstatsion", 199.99),
                new Item("67890", "Macbook", 299.99),
                new Item("23456", "xbox", 399.99),
                new Item("78901", "Dell Inspiron", 499.99),
                new Item("34567", "Hp power", 599.99)
        );
    }

    private void setInitialData() {
        itemRepository.deleteAll()
                .thenMany(Flux.fromIterable(getItems()))
                .flatMap(itemRepository::save)
                .thenMany(itemRepository.findAll())
                .subscribe(item -> {
                    System.out.println("inserted : " + item);
                });
    }
}
