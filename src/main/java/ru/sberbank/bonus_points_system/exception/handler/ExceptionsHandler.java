package ru.sberbank.bonus_points_system.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.PropertyAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.sberbank.bonus_points_system.exception.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError notExistHandle(NotExistException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler({AlreadyExistException.class, InsufficientBonusException.class})
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError refusedRequestHandle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseError dataIntegrityViolationExceptionHandle(DataIntegrityViolationException exception) {
        Throwable specificCause = exception.getMostSpecificCause();
        log.error(specificCause.getClass() + " : " + specificCause.getMessage());
        return new ResponseError(specificCause, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseError accessExceptionHandle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception, HttpStatus.UNAUTHORIZED);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, PropertyAccessException.class,
            HttpMessageConversionException.class, IllegalArgumentException.class, IllegalAccrualOperation.class,
            DoubleOperationException.class})
    public ResponseError badRequestHandle(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseError(exception, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ResponseError defaultErrorHandle(Throwable throwable) {
        log.error(throwable.getClass().getName() + " : " + throwable.getMessage());
        return new ResponseError(throwable, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
