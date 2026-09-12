package com.example.fruitmachine.domain;

/**
 * Default rule set for the classic game
 */
public class DefaultPrizeEvaluator implements PrizeEvaluator {

    @Override
    public PrizeType evaluate(SpinResult spin, int k) {
        // TODO: return the single best prize once the detectors below are implemented.
        return PrizeType.NONE;
    }

    /** All slots the same colour. */
    private boolean isJackpot(SpinResult spin) {
        throw new UnsupportedOperationException("TODO Part 1: implement jackpot detection");
    }

    /** Every slot a distinct colour. */
    private boolean isFullHouse(SpinResult spin) {
        throw new UnsupportedOperationException("TODO Part 2: implement full-house detection");
    }

    /** A run of at least {@code k} adjacent slots sharing a colour */
    private boolean hasRunOfAtLeast(SpinResult spin, int k) {
        throw new UnsupportedOperationException("TODO Part 3: implement adjacent-run detection");
    }
}
