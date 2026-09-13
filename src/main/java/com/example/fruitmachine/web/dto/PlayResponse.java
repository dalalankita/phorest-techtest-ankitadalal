package com.example.fruitmachine.web.dto;

import com.example.fruitmachine.domain.PlayResult;
import com.example.fruitmachine.domain.PrizeType;

import java.util.List;

/** Result of POST /machine/play. */
public record PlayResponse(
        List<Integer> slots,
        PrizeType prize,
        long payout,
        long freePlaysCredited,
        long currentFloat,
        long freePlaysBalance
) {
    public static PlayResponse from(PlayResult result) {
        return new PlayResponse(
                result.spin().colours(),
                result.prize(),
                result.payout(),
                result.freePlaysCredited(),
                result.resultingFloat(),
                result.freePlaysBalance()
        );
    }
}