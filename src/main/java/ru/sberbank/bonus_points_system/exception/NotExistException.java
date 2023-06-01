package ru.sberbank.bonus_points_system.exception;

public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super(message + " not found");
    }
}
