package ru.sberbank.bonus_points_system.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ResponseError {

    private final HttpStatus status;
    private final String exception;
    private final String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime time = LocalDateTime.now();

    public ResponseError(Throwable throwable, HttpStatus status) {
        this.exception = throwable.getClass().getName();
        this.message = throwable.getMessage();
        this.status = status;

    }
}
