package com.example.fruitmachine.domain;

import java.util.List;

/**
 * Default rule set for the classic game
 */
public class DefaultPrizeEvaluator implements PrizeEvaluator {

    @Override
    public PrizeType evaluate(SpinResult spin, int k) {
        if (isJackpot(spin)) {
            return PrizeType.JACKPOT;
        }
        return PrizeType.NONE;
    }

    /** All slots the same colour. */
    private boolean isJackpot(SpinResult spin) {
        List<Integer> coloursList = spin.colours();
        int firstColour = coloursList.getFirst();
        for (int colour: coloursList) {
            if (colour != firstColour) {
                return false;
            }
        }
        return true;
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
