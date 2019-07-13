package ru.restaurant.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurant.voting.model.Dish;
import ru.restaurant.voting.model.Menu;
import ru.restaurant.voting.model.Restaurant;
import ru.restaurant.voting.model.Vote;
import ru.restaurant.voting.service.*;
import ru.restaurant.voting.to.MenuTo;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.restaurant.voting.util.Util.orElse;

@RestController
@RequestMapping(value = RestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    static final String REST_URL = "rest/restaurant";

    @Autowired
    RestaurantService service;

    @Autowired
    MenuService menuService;

    @Autowired
    DishService dishService;

    @Autowired
    VoteService voteService;

    @GetMapping
    public List<Restaurant> getAll() {
        return service.getAllRestaurants();
    }

    @GetMapping(value = "/{id}")
    public Restaurant get(@PathVariable int id) {
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {

        Restaurant create = service.create(restaurant);
        URI uriNeResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(create.getId()).toUri();

        return ResponseEntity.created(uriNeResource).body(create);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateRestaurant(@RequestBody Restaurant restaurant, @PathVariable int id) {
        service.update(restaurant, id);
    }

    @DeleteMapping("/id")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteRestaurant(@PathVariable int id) {
        service.delete(id);
    }

    @GetMapping(value = "/{id}/menu")
    public Menu getMenu(@PathVariable int id,
                        @RequestParam(value = "date", required = false) LocalDate date) {
        return menuService.getRestaurantAndDishes(orElse(date, LocalDate.now()), id);
    }

    @PutMapping(value = "/{id}/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateMenu(@PathVariable int id,
                           @RequestParam(value = "date") LocalDate date,
                           @RequestBody MenuTo menuTo) {
        menuService.updateDishes(orElse(date, LocalDate.now()), id, menuTo.getDishes());
    }

    @GetMapping(value = "/menu/dishes/{id}")
    public Dish getDishes(@PathVariable int id) {
        return dishService.get(id);
    }

    @PostMapping(value = "/{id}/menu/dishes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createDishWithLocation(@PathVariable int id,
                                                       @Valid @RequestBody Dish dish,
                                                       @RequestParam(value = "date", required = false) LocalDate date) {

        Dish create = dishService.create(dish, id, orElse(date, LocalDate.now()));

        URI uriNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "menu/dishes/{id}")
                .buildAndExpand(create.getId()).toUri();
        return ResponseEntity.created(uriNewResource).body(create);
    }

    @PutMapping(value = "/menu/dishes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateDish(@PathVariable int id,
                           @Valid @RequestBody Dish dish) {
        dishService.update(dish, id);
    }

    @DeleteMapping("/menu/dishes/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteDish(@PathVariable int id) {
        dishService.delete(id);
    }

    @PutMapping(value = "{id}/vote")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public ResponseEntity<Vote> vote(@PathVariable int id) {
        VoteService.UpdatedVote updatedVote = voteService.createOrUpdate(id, SecurityUtil.authUserId());

        if (updatedVote.isCreated()) {
            URI uriNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(REST_URL + "/{id}/vote")
                    .buildAndExpand(updatedVote.getVote().getId()).toUri();

            return ResponseEntity.created(uriNewResource).body(updatedVote);
        } else {
            return new ResponseEntity<Vote>(HttpStatus.OK);
        }
    }
}
