package org.example.Controller;


import org.example.Model.Shop;
import org.example.Repo.ShopRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ShopController {

    private final ShopRepository shopRepository;

    public ShopController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @GetMapping("/shops")
    List<Shop> all() {
        return (List<Shop>) shopRepository.findAll();
    }

    @GetMapping("/")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "Hendrik") String name) {
        return new Greeting(1, String.format("Hey, %s!", name));
    }


    private record Greeting(long id, String content) {
    }
}
