package com.example.fruitmachine.domain;

import java.util.random.RandomGenerator;

public class RandomColourSelector implements ColourSelector {

    private final RandomGenerator random;

    public RandomColourSelector() {
        this(RandomGenerator.getDefault());
    }

    public RandomColourSelector(RandomGenerator random) {
        this.random = random;
    }

    @Override
    public int nextColour(int colourCount) {
        return random.nextInt(colourCount);
    }
}
