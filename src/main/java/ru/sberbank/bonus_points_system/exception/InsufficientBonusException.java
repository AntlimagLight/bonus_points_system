package ru.sberbank.bonus_points_system.exception;

public class InsufficientBonusException extends RuntimeException {
    public InsufficientBonusException(String message) {
        super(message);
    }
}
