package com.example.fruitmachine.domain;

/** Thrown when a machine is configured with values that break the game's invariants. */
public class InvalidMachineConfigException extends RuntimeException {
    public InvalidMachineConfigException(String message) {
        super(message);
    }
}
