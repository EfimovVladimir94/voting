package ru.restaurant.voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurant.voting.model.Menu;
import ru.restaurant.voting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {


    @EntityGraph(attributePaths = {"restaurant", "dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.date=:date ORDER BY m.restaurant.id")
    List<Menu> getAllRestaurantAndDishes(@Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"restaurant", "dishes"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m FROM Menu m WHERE m.date=?1 AND m.restaurant=?2")
    Optional<Menu> getWithRestaurantAndDishes(LocalDate date, Restaurant restaurant);

    Optional<Menu> findByDateAndRestaurant(LocalDate date, Restaurant restaurant);

}
