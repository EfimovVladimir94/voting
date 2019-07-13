package ru.restaurant.voting.util.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String message;
    private final String[] details;

    public ErrorInfo(CharSequence url, ErrorType type, String message, String... details) {
        this.url = url.toString();
        this.type = type;
        this.message = message;
        this.details = details;
    }
}
