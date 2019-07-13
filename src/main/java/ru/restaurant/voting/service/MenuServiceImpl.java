package ru.restaurant.voting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.restaurant.voting.model.Dish;
import ru.restaurant.voting.model.Menu;
import ru.restaurant.voting.model.Restaurant;
import ru.restaurant.voting.repository.MenuRepository;
import ru.restaurant.voting.repository.RestaurantRepository;
import ru.restaurant.voting.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurant.voting.util.ValidationUtil.checkMenuDateBeforeUpdate;
import static ru.restaurant.voting.util.ValidationUtil.checkNotFound;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    MenuRepository repository;

    @Autowired
    VoteService voteService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Cacheable("menu")
    @Override
    public List<Menu> getAllWithRestaurantAndDishes(LocalDate date) {
        Assert.notNull(date, "date must not be null");
        return repository.getAllWithRestaurantAndDishes(date);
    }

    @Override
    public Menu getWithRestaurantAndDishes(LocalDate date, int restaurantId) throws NotFoundException {
        Assert.notNull(date, "date must not be null");
        return checkNotFound(repository.getWithRestaurantAndDishes(date, restaurantRepository.getOne(restaurantId)),
                "menu not found for restaurant id=" + restaurantId + ", date=" + date);
    }

    @CacheEvict(value = "menu", allEntries = true)
    @Transactional
    @Override
    public void updateDishes(LocalDate date, int restaurantId, List<Dish> dishes) {
        Assert.notNull(dishes, "list dishes must not be null");
        checkNotFound(restaurantRepository.existsById(restaurantId), "id=" + restaurantId);
        checkMenuDateBeforeUpdate(date);
        Menu menu = repository.getWithRestaurantAndDishes(date, restaurantRepository.getOne(restaurantId))
                .orElseGet(() -> repository.save(new Menu(date, restaurantRepository.getOne(restaurantId))));
        dishes.forEach(dish -> dish.setMenu(menu));
        //menu.setDishes(dishes); // not work https://stackoverflow.com/questions/5587482
        menu.getDishes().clear();
        menu.getDishes().addAll(dishes);
        repository.save(menu);
    }

    @Override
    public Menu get(Restaurant restaurant, LocalDate date) throws NotFoundException {
        Assert.notNull(restaurant, "restaurant must not be null");
        Assert.notNull(date, "date must not be null");
        return checkNotFound(repository.findByDateAndRestaurant(date, restaurant),
                "menu not found for restaurant=" + restaurant + ", date=" + date);
    }

    @Override
    public Menu get(Restaurant restaurant) throws NotFoundException {
        return get(restaurant, LocalDate.now());
    }
}
