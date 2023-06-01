package ru.sberbank.bonus_points_system.util;

import lombok.experimental.UtilityClass;
import ru.sberbank.bonus_points_system.exception.AlreadyExistException;
import ru.sberbank.bonus_points_system.exception.NotExistException;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@UtilityClass
public class ValidationUtils {

    public static <T> T assertExistence(Optional<T> opt, String exceptionMassage) throws NotExistException {
        if (opt.isEmpty()) {
            throw new NotExistException(exceptionMassage);
        }
        return opt.get();
    }

    public static <T> T assertExistence(T entity, String exceptionMassage) throws NotExistException {
        if (entity == null) {
            throw new NotExistException(exceptionMassage);
        }
        return entity;
    }

    public static <T> void assertNotExistence(Optional<T> opt, String exceptionMassage) throws AlreadyExistException {
        if (opt.isPresent()) {
            throw new AlreadyExistException(exceptionMassage);
        }
    }

}