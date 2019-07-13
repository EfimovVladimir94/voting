package ru.restaurant.voting.util;

import org.slf4j.Logger;
import ru.restaurant.voting.HasId;
import ru.restaurant.voting.util.exception.ErrorType;
import ru.restaurant.voting.util.exception.IllegalRequestDataException;
import ru.restaurant.voting.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

public class ValidationUtil {

    public ValidationUtil() {
    }

    public static <T> T checkNotFoundWithId(Optional<T> object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        return checkNotFound(object, "id=" + id);
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id+" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String arg) {
        if (!found) {
            throw new NotFoundException(arg);
        }
    }

    public static <T> T checkNotFound(Optional<T> object, String msg) {
        return object.orElseThrow(() -> new NotFoundException(msg));
    }

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.getId() != id) {
            throw new IllegalRequestDataException(bean + "must be with id= " + id);
        }
    }

    public static LocalDate checkMenuDateBeforeUpdate(LocalDate date) {
        if (LocalDate.now().isAfter(date)) {
            throw new IllegalRequestDataException("it is forbidden to change the menu of the previous period");
        }
        return date;
    }

    public static Throwable getRootCause(Throwable th) {
        Throwable result = th;
        Throwable cause;
        while (null != (cause = result.getCause()) && (result != cause)) {
            result = cause;
        }
        return result;
    }

    public static String getMessage(Throwable e) {
        return e.getLocalizedMessage() != null ? e.getLocalizedMessage() : e.getClass().getName();
    }

}
