package ru.restaurant.voting;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.restaurant.voting.model.User;
import ru.restaurant.voting.web.Json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class TestUtil {
    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJsonResultActions(ResultActions actions, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(actions.andReturn()), clazz);
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult results, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(results), clazz);
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

}
