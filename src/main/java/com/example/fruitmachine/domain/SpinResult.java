package com.example.fruitmachine.domain;

import java.util.List;

/**
 * The colours shown after a spin, left-to-right.
 */
public record SpinResult(List<Integer> colours) {

    public SpinResult {
        colours = List.copyOf(colours);
    }

    public int slotCount() {
        return colours.size();
    }
}
