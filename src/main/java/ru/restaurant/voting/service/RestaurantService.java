package ru.restaurant.voting.service;

import ru.restaurant.voting.model.Restaurant;
import ru.restaurant.voting.util.exception.NotFoundException;

import java.util.List;

public interface RestaurantService {

    Restaurant get(int id) throws NotFoundException;

    List<Restaurant> getAllRestaurants();

    Restaurant create(Restaurant restaurant);

    void update(Restaurant restaurant, int id);

    void delete(int id) throws NotFoundException;


}
