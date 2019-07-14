package ru.restaurant.voting;

import ru.restaurant.voting.model.*;
import ru.restaurant.voting.to.MenuTo;


import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.time.LocalDate.of;
import static ru.restaurant.voting.model.AbstractBaseEntity.START_SEQ;

public class TestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final User USER = new User(USER_ID, "User", "user@yandex.ru", "password", Role.ROLE_USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "admin@gmail.com", "admin", Role.ROLE_ADMIN, Role.ROLE_USER);

    public static final Restaurant RESTAURANT_100002 = new Restaurant(100002, "Нихром");
    public static final Restaurant RESTAURANT_100003 = new Restaurant(100003, "Makiaveli");
    public static final Restaurant RESTAURANT_100004 = new Restaurant(100004, "Кампай");
    public static final Restaurant RESTAURANT_100005 = new Restaurant(100005, "Хан Сарай");

    public static final Dish DISH_100010 = new Dish(100010, "Блины", 6000, null);
    public static final Dish DISH_100011 = new Dish(100011, "Салат", 5000, null);
    public static final Dish DISH_100012 = new Dish(100012, "Суп", 6000, null);
    public static final Dish DISH_100013 = new Dish(100013, "Пицца", 4200, null);

    public static final Menu MENU_100006 = new Menu(100006, of(2019, Month.JULY, 11), RESTAURANT_100002, List.of(DISH_100010));
    public static final Menu MENU_100007 = new Menu(100007, of(2019, Month.JULY, 11), RESTAURANT_100003, List.of(DISH_100011));
    public static final Menu MENU_100008 = new Menu(100008, LocalDate.now(), RESTAURANT_100002, List.of(DISH_100012));
    public static final Menu MENU_100009 = new Menu(100009, LocalDate.now(), RESTAURANT_100003, List.of(DISH_100013));

    public static final int VOTE100014_ID = START_SEQ + 14;
    public static final Vote VOTE_100014 = new Vote(VOTE100014_ID, of(2019, Month.JULY, 11), MENU_100006, USER);

    public static Vote getCreatedVote() {
        return new Vote(null, LocalDate.now(), MENU_100008, USER);
    }

    public static Restaurant getCreatedRestaurant() {
        return new Restaurant(null, "New");
    }

    public static Restaurant getUpdatedRestaurant() {
        return new Restaurant(RESTAURANT_100003.getId(), RESTAURANT_100003.getName() + " Updated");
    }

    public static MenuTo getUpdatedMenuTo() {
        return new MenuTo(List.of(
                new Dish(null, "Роллы", 1500, null),
                new Dish(null, "Лагман", 3200, null),
                new Dish(null, "Устрицы", 8300, null)));
    }

    public static Dish getCreatedDish() {
        return new Dish(null, "new", 100, null);
    }

    public static Dish getUpdatedDish() {
        return new Dish(DISH_100013.getId(), "UPDATED " + DISH_100013.getName(), 12345, null);
    }

}