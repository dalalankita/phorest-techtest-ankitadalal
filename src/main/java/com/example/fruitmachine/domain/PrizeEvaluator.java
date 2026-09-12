package com.example.fruitmachine.domain;

/**
 * Decides which prize (if any) a spin has won.
 */
public interface PrizeEvaluator {

    /**
     * @param spin the spin to evaluate
     * @param k    minimum run length for a small prize (2 in the classic game)
     * @return the single best prize for this spin, honouring precedence
     */
    PrizeType evaluate(SpinResult spin, int k);
}
