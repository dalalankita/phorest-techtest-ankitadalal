package com.example.fruitmachine.domain;

/**
 * Supplies the colour for a single slot.
 */
@FunctionalInterface
public interface ColourSelector {

    /**
     * @param colourCount number of distinct colours available (ids are 0 .. colourCount-1)
     * @return a colour id in the range [0, colourCount]
     */
    int nextColour(int colourCount);
}
