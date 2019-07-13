package ru.restaurant.voting.service;

import ru.restaurant.voting.model.Menu;
import ru.restaurant.voting.model.Dish;
import ru.restaurant.voting.model.Restaurant;
import ru.restaurant.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface MenuService {

    List<Menu> getAllWithRestaurantAndDishes(LocalDate date);

    Menu getWithRestaurantAndDishes(LocalDate date, int restaurantId) throws NotFoundException;

    void updateDishes(LocalDate date, int restaurantId, List<Dish> dishes);

    Menu get (Restaurant restaurant, LocalDate date) throws NotFoundException;

    Menu get(Restaurant restaurant) throws NotFoundException;

}
