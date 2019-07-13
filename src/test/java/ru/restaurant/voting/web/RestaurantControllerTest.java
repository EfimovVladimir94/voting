package ru.restaurant.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.restaurant.voting.model.Dish;
import ru.restaurant.voting.model.Menu;
import ru.restaurant.voting.model.Restaurant;
import ru.restaurant.voting.model.Vote;
import ru.restaurant.voting.repository.DishRepository;
import ru.restaurant.voting.service.DishService;
import ru.restaurant.voting.service.MenuService;
import ru.restaurant.voting.service.RestaurantService;
import ru.restaurant.voting.service.VoteService;
import ru.restaurant.voting.to.MenuTo;
import ru.restaurant.voting.web.Json.JsonUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurant.voting.TestData.*;
import static ru.restaurant.voting.TestUtil.*;

class RestaurantControllerTest extends AbstractControllerTest {
    private static final String REST_URL = RestaurantController.REST_URL + '/';

    @Autowired
    RestaurantService service;

    @Autowired
    MenuService menuService;

    @Autowired
    VoteService voteService;

    @Autowired
    DishService dishService;

    @Autowired
    DishRepository dishRepository;

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(service.getAllRestaurants())
                        .isEqualTo(List.of(RESTAURANT_100003, RESTAURANT_100004, RESTAURANT_100002, RESTAURANT_100005)));

    }

    @Test
    void testGet() throws Exception {
        final int id = RESTAURANT_100003.getId();
        mockMvc.perform(get(REST_URL + id)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(service.get(id)).isEqualTo(RESTAURANT_100003));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 123456)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testCreateWithLocation() throws Exception {
        Restaurant created = getCreatedRestaurant();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isCreated());
        Restaurant returned = readFromJsonResultActions(action, Restaurant.class);
        created.setId(returned.getId());

        assertThat(returned).isEqualTo(created);
        assertThat(service.getAllRestaurants())
                .isEqualTo(List.of(RESTAURANT_100003, created, RESTAURANT_100004, RESTAURANT_100002, RESTAURANT_100005));
    }

    @Test
    void testUpdate() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        mockMvc.perform(put(REST_URL + RESTAURANT_100003.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertThat(service.get(RESTAURANT_100003.getId())).isEqualTo(updated);
    }

    @Test
    void updateNotFound() throws Exception {
        Restaurant updated = getUpdatedRestaurant();
        mockMvc.perform(put(REST_URL + 123456)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
        assertThat(service.getAllRestaurants())
                .isEqualTo(List.of(RESTAURANT_100003, RESTAURANT_100004, RESTAURANT_100002, RESTAURANT_100005));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + RESTAURANT_100005.getId())
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThat(service.getAllRestaurants()).isEqualTo(List.of(RESTAURANT_100003, RESTAURANT_100004, RESTAURANT_100002));
    }

    @Test
    void deleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + 123456)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        assertThat(service.getAllRestaurants()).isEqualTo(List.of(RESTAURANT_100003, RESTAURANT_100004, RESTAURANT_100002, RESTAURANT_100005));
    }

    @Test
    void getMenu() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT_100002.getId() + "/menu")
                .param("date", "2019-07-11")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Menu.class)).isEqualTo(MENU_100006));
    }

    @Test
    void getMenuForToday() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT_100002.getId() + "/menu")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(readFromJsonMvcResult(result, Menu.class)).isEqualTo(MENU_100008));
    }

    @Test
    void getMenuNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + RESTAURANT_100002.getId() + "/menu")
                .param("date", "2099-01-01")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateMenu() throws Exception {
        MenuTo updated = getUpdatedMenuTo();
        mockMvc.perform(put(REST_URL + RESTAURANT_100003.getId() + "/menu")
                .param("date", "2099-01-01")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThat(menuService.getRestaurantAndDishes(LocalDate.parse("2099-01-01"), RESTAURANT_100003.getId()).getDishes())
                .usingElementComparatorIgnoringFields("id", "menu")
                .isEqualTo(updated.getDishes());
    }

    @Test
    void updateMenuIllegalDate() throws Exception {
        mockMvc.perform(put(REST_URL + RESTAURANT_100003.getId() + "/menu")
                .param("date", "2018-01-01")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getUpdatedMenuTo()))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void createDishWithLocation() throws Exception {
        Dish created = getCreatedDish();
        ResultActions resultActions = mockMvc.perform(post(REST_URL + RESTAURANT_100003.getId() + "/menu/dishes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isCreated());
        Dish returned = readFromJsonResultActions(resultActions, Dish.class);
        created.setId(returned.getId());
        assertThat(returned).isEqualTo(created);

        assertThat(dishRepository.findAllDishByMenuId(MENU_100009.getId()))
                .isEqualTo(List.of(DISH_100013, created));

    }

    @Test
    void getDish() throws Exception {
        mockMvc.perform(get(REST_URL + "/menu/dishes/" + DISH_100013.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(dishService.get(DISH_100013.getId())).isEqualTo(DISH_100013));
    }

    @Test
    void getDishNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + "/menu/dishes/123456")
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void updateDish() throws Exception {
        Dish updated = getUpdatedDish();
        mockMvc.perform(put(REST_URL + "/menu/dishes/" + DISH_100013.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertThat(dishService.get(DISH_100013.getId())).isEqualTo(updated);
    }

    @Test
    void deleteDish() throws Exception {
        mockMvc.perform(delete(REST_URL + "/menu/dishes/" + DISH_100013.getId())
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertThat(dishRepository.findAllDishByMenuId(MENU_100009.getId()))
                .isEqualTo(Collections.emptyList());
    }


    @Test
    void vote() throws Exception {
        ResultActions resultActions = mockMvc.perform(put(REST_URL + RESTAURANT_100003.getId() + "/vote")
                .with(userHttpBasic(USER)))
                .andExpect(status().isCreated());
        Vote returned = readFromJsonResultActions(resultActions, Vote.class);
        Vote created = getCreatedVote();
        created.setId(returned.getId());
        assertThat(returned).isEqualTo(created);
        LocalDate now = LocalDate.now();
        assertThat(voteService.getVoteIsBetweenDate(USER_ID, now, now)).isEqualTo(List.of(created));
    }

    @Test
    void testGetAllForbidden() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

}