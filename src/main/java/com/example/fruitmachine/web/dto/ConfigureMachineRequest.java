package com.example.fruitmachine.web.dto;

import com.example.fruitmachine.domain.MachineConfig;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Incoming body for configuring a machine. Shape checks here; game rules in MachineConfig. */
public record ConfigureMachineRequest(
        @NotNull @Min(1) Integer slotCount,
        @NotNull @Min(1) Integer colourCount,
        @NotNull @Min(1) Integer k,
        @NotNull @Min(0) Long playCost,
        @NotNull @Min(0) Long startingFloat
) {
    public MachineConfig toDomain() {
        return new MachineConfig(slotCount, colourCount, k, playCost, startingFloat);
    }
}