package com.example.fruitmachine.domain;

import com.example.fruitmachine.domain.support.ScriptedColourSelector;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FruitMachineTest {

    private FruitMachine machine(MachineConfig config, Integer... scriptedColours) {
        return new FruitMachine(config, new ScriptedColourSelector(scriptedColours), new DefaultPrizeEvaluator());
    }

    @Test
    void new_machine_starts_with_the_configured_float() {
        FruitMachine machine = machine(MachineConfig.classic(1, 100));

        assertThat(machine.currentFloat()).isEqualTo(100);
        assertThat(machine.freePlays()).isZero();
    }

    @Test
    void spin_uses_the_selector_to_fill_every_slot() {
        SpinResult spinResult = machine(MachineConfig.classic(1,100), 0, 1, 2, 3).spin();

        assertThat(spinResult.colours()).containsExactly(0,1,2,3);
        assertThat(spinResult.slotCount()).isEqualTo(4);
    }

    @Test
    void jackpot_when_all_slots_share_a_colour() {
        PlayResult playResult = machine(MachineConfig.classic(1,100), 1, 1, 1, 1).play();

        assertThat(playResult.prize()).isEqualTo(PrizeType.JACKPOT);
        assertThat(playResult.payout()).isEqualTo(100);
        assertThat(playResult.resultingFloat()).isZero();
        assertThat(playResult.freePlaysCredited()).isZero();
    }

    @Test
    void no_jackpot_when_slots_differ() {
        PlayResult playResult = machine(MachineConfig.classic(1,100), 1, 1, 1, 2).play();

        assertThat(playResult.prize()).isEqualTo(PrizeType.NONE);
        assertThat(playResult.payout()).isZero();
        assertThat(playResult.resultingFloat()).isEqualTo(100);

    }

    @Test
    void full_house_when_all_slot_are_different() {
        PlayResult playResult = machine(new MachineConfig(4,4,2,2,100),0, 1, 2, 3)
                .play();

        assertThat(playResult.prize()).isEqualTo(PrizeType.FULL_HOUSE);
        assertThat(playResult.payout()).isEqualTo(50);
        assertThat(playResult.resultingFloat()).isEqualTo(50);
    }

    @Test
    @Disabled("TODO Part 2: full house pays half the float; small prize pays 5 x cost")
    void payouts_per_prize_type() {
        // ...
    }

    @Test
    @Disabled("TODO Part 2: shortfall credits free plays, but NOT for a jackpot")
    void prize_larger_than_float_credits_free_plays() {
        // ...
    }

    @Test
    @Disabled("TODO Part 3: k adjacent, many colours, large slot counts")
    void generalises_to_arbitrary_k_and_colours() {
        // ...
    }
}
