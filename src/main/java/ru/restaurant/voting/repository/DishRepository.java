package ru.restaurant.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant.voting.model.Dish;

import java.util.List;

@Transactional(readOnly = true)
public interface DishRepository extends JpaRepository<Dish,Integer> {

    @Transactional
    @Override
    Dish save(Dish dish);

    List<Dish> findAllDishByMenuId(int menuId);
}
