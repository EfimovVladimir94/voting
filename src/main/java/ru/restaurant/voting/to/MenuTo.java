package ru.restaurant.voting.to;

import ru.restaurant.voting.model.Dish;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class MenuTo {

    @Valid
    @NotNull
    private List<Dish> dishes;

    public MenuTo() {
    }

    public MenuTo(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
