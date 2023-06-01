package ru.sberbank.bonus_points_system.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message + " already exist");
    }
}
