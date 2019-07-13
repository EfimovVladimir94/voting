package ru.restaurant.voting.util.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {
    public static final String NOT_FOUND_ERROR = "exception.common.notFound";

    public NotFoundException(String args) {
        super(ErrorType.APP_ERROR, NOT_FOUND_ERROR, HttpStatus.UNPROCESSABLE_ENTITY, args);
    }
}
