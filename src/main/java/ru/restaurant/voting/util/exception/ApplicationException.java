package ru.restaurant.voting.util.exception;


import org.springframework.http.HttpStatus;

import java.util.Arrays;

public class ApplicationException extends RuntimeException {
    private final ErrorType type;
    private final String msg;
    private final HttpStatus status;
    private final String[] args;

    public ApplicationException(ErrorType type, String msg, HttpStatus status, String... args) {
        super(String.format("type=%s, msg=%s, args=%s", type, msg, Arrays.toString(args)));
        this.type = type;
        this.msg = msg;
        this.status = status;
        this.args = args;
    }

    public ErrorType getType() {
        return type;
    }

    public String getMsg() {
        return msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String[] getArgs() {
        return args;
    }
}
