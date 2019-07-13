package ru.restaurant.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.restaurant.voting.model.Dish;
import ru.restaurant.voting.model.Menu;
import ru.restaurant.voting.model.Restaurant;
import ru.restaurant.voting.repository.MenuRepository;
import ru.restaurant.voting.repository.RestaurantRepository;
import ru.restaurant.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurant.voting.util.ValidationUtil.*;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepository repository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Cacheable("menu")
    @Override
    public List<Menu> getAllRestaurantsAndDishes(LocalDate date) {
        Assert.notNull(date, "date must not be null");
        return repository.getAllRestaurantAndDishes(date);
    }

    @Override
    public Menu getRestaurantAndDishes(LocalDate date, int restaurantId) throws NotFoundException {
        Assert.notNull(date, "date must not be null");
        return checkNotFound(repository.getWithRestaurantAndDishes(date, restaurantRepository.getOne(restaurantId)), "menu not found for restaurant=" + restaurantId + ", date=" + date);
    }

    @CacheEvict(value = "menu", allEntries = true)
    @Override
    public void updateDishes(LocalDate date, int restaurantId, List<Dish> dishes) throws NotFoundException {
        Assert.notNull(date, "date must not be null");
        checkNotFound(restaurantRepository.existsById(restaurantId), "id=" + restaurantId);
        checkMenuDateBeforeUpdate(date);
        Menu menu = repository.getWithRestaurantAndDishes(date,restaurantRepository.getOne(restaurantId))
                .orElseGet(()->repository.save(new Menu(date,restaurantRepository.getOne(restaurantId))));
        dishes.forEach(dish -> dish.setMenu(menu));
        menu.getDishes().clear();
        menu.getDishes().addAll(dishes);
        repository.save(menu);
    }

    @Override
    public Menu get(Restaurant restaurant, LocalDate date) throws NotFoundException {
        Assert.notNull(restaurant, "restaurant must not be null");
        Assert.notNull(date, "date must not be null");
        return checkNotFound(repository.findByDateAndRestaurant(date, restaurant), "menu not found for restaurant=" + restaurant + ", date=" + date);
    }

    @Override
    public Menu get(Restaurant restaurant) throws NotFoundException {
        return get(restaurant, LocalDate.now());
    }
}
