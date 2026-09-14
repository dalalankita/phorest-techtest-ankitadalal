package com.example.fruitmachine.domain;

/**
 * Immutable configuration for a machine.
 */
public record MachineConfig(
        int slotCount,
        int colourCount,
        int k,
        long playCost,
        long startingFloat
) {
    public MachineConfig {
        if (slotCount < 3) {
            throw new InvalidMachineConfigException("slotCount must be >= 3");
        }
        if (colourCount < 1) {
            throw new InvalidMachineConfigException("colourCount must be >= 1");
        }
        if (k < 2) {
            throw new InvalidMachineConfigException("k must be >= 2");
        }
        if (k > slotCount) {
            throw new InvalidMachineConfigException("k cannot exceed slotCount");
        }
        if (playCost < 1) {
            throw new InvalidMachineConfigException("playCost must be >= 1");
        }
        if (startingFloat < 0) {
            throw new InvalidMachineConfigException("startingFloat must be >= 0");
        }
    }

    public static MachineConfig classic(long playCost, long startingFloat) {
        return new MachineConfig(4, 4, 2, playCost, startingFloat);
    }
}
