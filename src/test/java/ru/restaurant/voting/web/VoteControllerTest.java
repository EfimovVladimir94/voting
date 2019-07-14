package ru.restaurant.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.restaurant.voting.service.VoteService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurant.voting.TestData.*;
import static ru.restaurant.voting.TestUtil.userHttpBasic;

class VoteControllerTest extends AbstractControllerTest {
    private static final String REST_URL = VoteController.REST_URL + '/';

    @Autowired
    VoteService service;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(service.getAll(USER_ID))
                        .isEqualTo(List.of(VOTE_100014)));
    }

    @Test
    void getBetween() throws Exception {
        mockMvc.perform(get(REST_URL + "/filter")
                .param("start", "2018-07-01")
                .param("end", "2018-07-31")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(service.getAll(USER_ID))
                        .isEqualTo(List.of(VOTE_100014)));
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + VOTE100014_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(result -> assertThat(service.get(VOTE100014_ID, USER_ID))
                        .isEqualTo(VOTE_100014));
    }

    @Test
    void getNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + 123456)
                .with(userHttpBasic(USER)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void testGetUnAuth() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isUnauthorized());
    }
}