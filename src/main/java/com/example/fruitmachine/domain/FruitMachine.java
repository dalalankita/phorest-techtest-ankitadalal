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
            coloursList.add(colourSelector.nextColour(config().colourCount()));
        }
        return new SpinResult(coloursList);
    }

    /**
     * Play one round: spin, evaluate, settle the money, and return the outcome.
     */
    public PlayResult play() {
        SpinResult spinResult = spin();
        PrizeType prize = prizeEvaluator.evaluate(spinResult, config.k());

        long payout = 0;
        long freePlayCredit = 0;

        switch (prize) {
            case JACKPOT -> {
                payout = currentFloat;
                currentFloat = 0;
            }
            case FULL_HOUSE -> {
                payout = currentFloat/2;
                currentFloat -= payout;
            }
            case SMALL_PRIZE -> {
                long amount = 5 * config.playCost();
                payout = Math.min(amount, currentFloat);

                long shortFall = amount - payout;
                freePlayCredit = shortFall/config.playCost();

                currentFloat -= payout;
                freePlays += freePlayCredit;
            }
        }
        return new PlayResult(spinResult, prize, payout, freePlayCredit, currentFloat, freePlays);
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
