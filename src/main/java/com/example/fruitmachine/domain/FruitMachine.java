package com.example.fruitmachine.domain;

import java.util.ArrayList;
import java.util.List;

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
        int slotCount = config.slotCount();
        List<Integer> coloursList = new ArrayList<>(slotCount);
        for (int i=0; i<slotCount;i++) {
            coloursList.add(colourSelector.nextColour(slotCount));
        }
        return new SpinResult(coloursList);
    }

    /**
     * Play one round: spin, evaluate, settle the money, and return the outcome.
     */
    public PlayResult play() {
        SpinResult spinResult = spin();
        PrizeType prize = prizeEvaluator.evaluate(spinResult, config.k());
        return new PlayResult(spinResult, prize, 0, 0, currentFloat, freePlays);
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
