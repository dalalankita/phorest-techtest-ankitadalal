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
        //Part 2 is the classic k = 2 game. Part 3 will use k for a run of k
        if (hasAdjacentPair(spin)) {
            return PrizeType.SMALL_PRIZE;
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

    /** Two or more adjacent slots the same colour. Part 3 generalizes this to a run of k. */
    private boolean hasAdjacentPair(SpinResult spin) {
        List<Integer> colourList = spin.colours();
        for (int i=1;i<colourList.size();i++) {
            if (colourList.get(i).equals(colourList.get(i-1))) {
                return true;
            }
        }
        return false;
    }
}
