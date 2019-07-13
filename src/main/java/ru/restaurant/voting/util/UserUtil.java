package ru.restaurant.voting.util;

import ru.restaurant.voting.model.User;
import ru.restaurant.voting.to.UserTo;

public class UserUtil {
    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword());
    }
}