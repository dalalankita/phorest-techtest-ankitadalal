package com.example.fruitmachine.web.dto;

import com.example.fruitmachine.domain.FruitMachine;
import com.example.fruitmachine.domain.MachineConfig;

/** The machine's current state, returned by GET /machine. */
public record MachineStateResponse(
        MachineConfig config,
        long currentFloat,
        long freePlays
) {
    public static MachineStateResponse from(FruitMachine machine) {
        return new MachineStateResponse(machine.config(), machine.currentFloat(), machine.freePlays());
    }
}