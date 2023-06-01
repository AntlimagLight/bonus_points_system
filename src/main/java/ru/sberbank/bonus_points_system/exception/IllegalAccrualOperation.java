package ru.sberbank.bonus_points_system.exception;

public class IllegalAccrualOperation extends RuntimeException {
    public IllegalAccrualOperation(String message) {
        super(message);
    }
}
