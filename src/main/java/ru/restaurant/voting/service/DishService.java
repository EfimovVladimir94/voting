package ru.restaurant.voting.service;

import ru.restaurant.voting.model.Dish;
import ru.restaurant.voting.util.exception.NotFoundException;

import java.time.LocalDate;

public interface DishService {

    Dish create(Dish dish, int restaurantId, LocalDate date);

    void update(Dish dish, int id);

    void delete(int id) throws NotFoundException;

    Dish get(int id) throws NotFoundException;

}
