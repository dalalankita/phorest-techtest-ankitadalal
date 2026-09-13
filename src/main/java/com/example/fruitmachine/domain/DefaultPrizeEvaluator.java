package com.example.fruitmachine.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Default rule set for the classic game
 */
public class DefaultPrizeEvaluator implements PrizeEvaluator {

    @Override
    public PrizeType evaluate(SpinResult spin, int k) {
        if (isJackpot(spin)) {
            return PrizeType.JACKPOT;
        }
        if (isFullHouse(spin)) {
            return PrizeType.FULL_HOUSE;
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
        Set<Integer> colourSet = new HashSet<>();
        for (int colour: spin.colours()) {
            if (!colourSet.add(colour)) {
                return false;
            }
        }
        return true;
    }

    /** A run of at least {@code k} adjacent slots sharing a colour */
    private boolean hasRunOfAtLeast(SpinResult spin, int k) {
        throw new UnsupportedOperationException("TODO Part 3: implement adjacent-run detection");
    }
}
