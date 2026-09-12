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
    @Disabled("TODO Part 1: spin fills every slot via the selector")
    void spin_uses_the_selector_to_fill_every_slot() {
        // ...
    }

    @Test
    @Disabled("TODO Part 1: all slots the same colour is a jackpot")
    void jackpot_when_all_slots_share_a_colour() {
        // ...
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
