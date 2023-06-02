package ru.sberbank.bonus_points_system.exception;

public class DoubleOperationException extends RuntimeException {
    public DoubleOperationException(String message) {
        super(message);
    }
}
