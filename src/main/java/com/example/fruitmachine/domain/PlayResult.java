package com.example.fruitmachine.domain;

/**
 * The outcome of a single play: what was shown, what was won, and the resulting machine state.
 *
 * @param spin               the colours shown
 * @param prize              which prize (if any) was won
 * @param payout             amount actually paid to the player from the float
 * @param freePlaysCredited  free plays granted this play because the float could not cover the
 *                           prize in full
 * @param resultingFloat     the float after this play
 * @param freePlaysBalance   the player's total free-play balance after this play
 */
public record PlayResult(
        SpinResult spin,
        PrizeType prize,
        long payout,
        long freePlaysCredited,
        long resultingFloat,
        long freePlaysBalance
) {
}
