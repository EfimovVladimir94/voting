package ru.restaurant.voting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.restaurant.voting.model.Menu;
import ru.restaurant.voting.service.MenuService;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurant.voting.util.Util.orElse;

@RestController
@RequestMapping(value = MenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    static final String REST_URL = "rest/menu";
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    MenuService service;

    @GetMapping
    public List<Menu> getAll(@RequestParam(required = false) LocalDate date) {
        log.info("getAll");
        return service.getAllRestaurantsAndDishes(orElse(date, LocalDate.now()));
    }
}
