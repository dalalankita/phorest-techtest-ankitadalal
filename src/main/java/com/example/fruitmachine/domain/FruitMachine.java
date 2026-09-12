package com.example.fruitmachine.domain;

/**
 * A single stateful machine: holds its configuration, current float, and free-play balance,
 * and knows how to play a round.
 */
public class FruitMachine {

    private final MachineConfig config;
    private final ColourSelector colourSelector;
    private final PrizeEvaluator prizeEvaluator;

    private long currentFloat;
    private long freePlays;

    public FruitMachine(MachineConfig config, ColourSelector colourSelector, PrizeEvaluator prizeEvaluator) {
        this.config = config;
        this.colourSelector = colourSelector;
        this.prizeEvaluator = prizeEvaluator;
        this.currentFloat = config.startingFloat();
    }

    /**
     * Randomly fill every slot.
     */
    public SpinResult spin() {
        throw new UnsupportedOperationException("TODO Part 1: implement spin()");
    }

    /**
     * Play one round: spin, evaluate, settle the money, and return the outcome.
     */
    public PlayResult play() {
        throw new UnsupportedOperationException("TODO Part 2: implement play()");
    }

    public MachineConfig config() {
        return config;
    }

    public long currentFloat() {
        return currentFloat;
    }

    public long freePlays() {
        return freePlays;
    }
}
