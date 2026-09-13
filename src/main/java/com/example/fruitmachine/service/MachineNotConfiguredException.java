package com.example.fruitmachine.service;

public class MachineNotConfiguredException extends RuntimeException {

    public MachineNotConfiguredException() {
        super("No machine has been configured yet");
    }
}
