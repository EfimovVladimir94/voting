package ru.restaurant.voting.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.restaurant.voting.util.ValidationUtil;
import ru.restaurant.voting.util.exception.ApplicationException;
import ru.restaurant.voting.util.exception.ErrorInfo;
import ru.restaurant.voting.util.exception.ErrorType;
import ru.restaurant.voting.util.exception.IllegalRequestDataException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class GlobalControllerException {

    public static final String EXCEPTION_DUPLICATE_VOTE = "exception.vote.duplicate";
    public static final String EXCEPTION_DUPLICATE_NAME = "exception.restaurant.duplicate";

    public static final Map<String, String> I18N_MAP = Map.of(
            "votes_unique_user_date_idx", EXCEPTION_DUPLICATE_VOTE,
            "restaurants_unique_name_idx", EXCEPTION_DUPLICATE_NAME);

    @Autowired
    MessageUtil messageUtil;

    //Status422
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorInfo> conflict(HttpServletRequest req, DataIntegrityViolationException e) {
        String[] details;
        String rootMsg = ValidationUtil.getRootCause(e).getMessage();
        if (rootMsg != null) {
            String lowerCaseMsg = rootMsg.toLowerCase();
            Optional<Map.Entry<String, String>> entry = I18N_MAP.entrySet().stream()
                    .filter(it -> lowerCaseMsg.contains(it.getKey()))
                    .findAny();
            details = new String[]{entry.isPresent() ? messageUtil.getMessage(entry.get().getValue()) : lowerCaseMsg};
        } else {
            details = null;
        }
        return getResponse(HttpStatus.CONFLICT, req, ErrorType.DATA_ERROR, e);
    }

    //422
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorInfo> bindValidationError(HttpServletRequest req, Exception e) {
        BindingResult result = e instanceof BindException ?
                ((BindException) e).getBindingResult() : ((MethodArgumentNotValidException) e).getBindingResult();

        String[] details = result.getFieldErrors().stream()
                .map(f -> messageUtil.getMessage(f))
                .toArray(String[]::new);

        return getResponse(HttpStatus.UNPROCESSABLE_ENTITY, req, ErrorType.VALIDATION_ERROR, details);
    }

    //Status 400
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorInfo> wrongRequest(HttpServletRequest req, NoHandlerFoundException e) throws Exception {
        return getResponse(HttpStatus.BAD_REQUEST, req, ErrorType.WRONG_REQUEST, e);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorInfo> applicationExceptionErrorHandler(HttpServletRequest req, ApplicationException appEx) {
        return getResponse(appEx.getStatus(), req, appEx.getType(), messageUtil.getMessage(appEx));
    }

    //Status500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> defaultErrorHandler(HttpServletRequest req, Exception e) {
        return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, req, ErrorType.APP_ERROR, e);
    }

    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorInfo> illegalRequestDataError(HttpServletRequest req, Exception e) {
        return getResponse(HttpStatus.UNPROCESSABLE_ENTITY, req, ErrorType.VALIDATION_ERROR, e);
    }

    private ResponseEntity<ErrorInfo> getResponse(HttpStatus status, HttpServletRequest req, ErrorType errorType, Exception e) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        return getResponse(status, req, errorType, ValidationUtil.getMessage(rootCause));
    }

    private ResponseEntity<ErrorInfo> getResponse(HttpStatus status, HttpServletRequest req, ErrorType errorType, String... details) {
        StringBuilder url = new StringBuilder(req.getRequestURL());
        if (req.getQueryString() != null) {
            url.append("?").append(req.getRequestURL());
        }
        return new ResponseEntity<>(new ErrorInfo(url, errorType,
                messageUtil.getMessage(errorType.getErrorCode()),
                details.length != 0 ? details : null), status);
    }
}
