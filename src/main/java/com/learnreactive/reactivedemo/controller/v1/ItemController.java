package com.learnreactive.reactivedemo.controller.v1;

import com.learnreactive.reactivedemo.document.Item;
import com.learnreactive.reactivedemo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.learnreactive.reactivedemo.config.Constants.VI_GET_ALL_ITEMS_ENDPOINT;

@RestController
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @GetMapping(VI_GET_ALL_ITEMS_ENDPOINT)
    public Flux<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping(VI_GET_ALL_ITEMS_ENDPOINT + "/{id}")
    public Mono<ResponseEntity<Item>> getItemById(@PathVariable String id) {
        return itemRepository.findById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(VI_GET_ALL_ITEMS_ENDPOINT + "/{id}")
    public Mono<ResponseEntity<Object>> deleteItemById(@PathVariable String id) {
        return itemRepository.findById(id)
                .map(item -> {
                    itemRepository.delete(item);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @PostMapping(VI_GET_ALL_ITEMS_ENDPOINT)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Item> createItem(@RequestBody Item item) {
        return itemRepository.save(item);
    }

    @PutMapping(VI_GET_ALL_ITEMS_ENDPOINT + "/{id}")
    public Mono<ResponseEntity<Item>> updateItem(@PathVariable String id,
                                                 @RequestBody Item item) {
        return itemRepository.findById(id)
                .flatMap(currentItem -> {
                    currentItem.setPrice(item.getPrice());
                    currentItem.setDescription(item.getDescription());
                    return itemRepository.save(currentItem);
                })
                .map(updatedItem -> new ResponseEntity<>(updatedItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
