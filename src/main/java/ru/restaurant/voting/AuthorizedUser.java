package ru.restaurant.voting;

import ru.restaurant.voting.model.User;
import ru.restaurant.voting.to.UserTo;
import ru.restaurant.voting.util.UserUtil;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    public static final long serialVersionUID = 1L;

    private UserTo userTo;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true, user.getRoles());
        this.userTo = UserUtil.asTo(user);
    }

    public int getId() {
        return userTo.getId();
    }

    public UserTo getUserTo() {
        return userTo;
    }

    @Override
    public String toString() {
        return userTo.toString();
    }
}
