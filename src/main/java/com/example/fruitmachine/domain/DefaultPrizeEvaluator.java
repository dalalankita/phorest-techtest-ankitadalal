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
        if (hasRunOfAtLeast(spin, k)) {
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

    /**
     * True if some run of at least k adjacent slots shares a colour. Single left-to-right pass
     * tracking the current run length, so it stays O(n) as the slot count grows.
     */
    private boolean hasRunOfAtLeast(SpinResult spin, int k) {
        List<Integer> colourList = spin.colours();
        int currRunLength = 1;
        for (int i=1;i<colourList.size();i++) {
            currRunLength = colourList.get(i).equals(colourList.get(i-1)) ? currRunLength + 1 : 1;
            if (currRunLength >= k) {
                return true;
            }
        }
        return false;
    }
}
