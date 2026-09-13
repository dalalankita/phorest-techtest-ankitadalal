package com.example.fruitmachine.domain;

import com.example.fruitmachine.domain.support.RecordingSelector;
import com.example.fruitmachine.domain.support.ScriptedColourSelector;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        PlayResult playResult = machine(MachineConfig.classic(1,100), 1, 2, 1, 2).play();

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
    void small_prize_when_adjacent_slots_match() {
        PlayResult playResult = machine(MachineConfig.classic(2, 100),0,0,1,2)
                .play();

        assertThat(playResult.prize()).isEqualTo(PrizeType.SMALL_PRIZE);
        assertThat(playResult.payout()).isEqualTo(10);
        assertThat(playResult.resultingFloat()).isEqualTo(90);
        assertThat(playResult.freePlaysCredited()).isZero();
    }

    @Test
    void prize_larger_than_float_credits_free_plays() {
        PlayResult playResult = machine(new MachineConfig(4,4,2,5,10),0,0,1,2)
                .play();

        assertThat(playResult.prize()).isEqualTo(PrizeType.SMALL_PRIZE);
        assertThat(playResult.payout()).isEqualTo(10);
        assertThat(playResult.resultingFloat()).isZero();
        assertThat(playResult.freePlaysCredited()).isEqualTo(15);
        assertThat(playResult.freePlaysBalance()).isEqualTo(15);
    }

    @Test
    void free_plays_accumulate_across_plays() {
        FruitMachine machine = machine(new MachineConfig(4, 4, 2, 5, 10),
                0, 0, 1, 2,
                0, 0, 1, 2);

        assertThat(machine.play().freePlaysBalance()).isEqualTo(15);
        assertThat(machine.play().freePlaysBalance()).isEqualTo(40);
    }

    @Test
    void full_house_with_odd_float() {
        PlayResult playResult = machine(new MachineConfig(4,4,2,2,101),0, 1, 2, 3)
                .play();

        assertThat(playResult.prize()).isEqualTo(PrizeType.FULL_HOUSE);
        assertThat(playResult.payout()).isEqualTo(50);
        assertThat(playResult.resultingFloat()).isEqualTo(51);
    }

    @Test
    void config_rejects_k_greater_than_slot_count() {
        assertThrows(InvalidMachineConfigException.class, () -> new MachineConfig(3,4,4,1,100));
    }

    @Test
    void small_prize_k_least_run(){
        MachineConfig k = new MachineConfig(5,9,3,2,100);

        PrizeType prize1 = machine(k,0,0,1,1,1).play().prize();
        assertThat(prize1).isEqualTo(PrizeType.SMALL_PRIZE);

        PrizeType prize2 = machine(k,0,0,1,1,0).play().prize();
        assertThat(prize2).isEqualTo(PrizeType.NONE);
    }

    @Test
    void spin_ask_selector_for_colour_within_request() {
        RecordingSelector selector = new RecordingSelector();
        new FruitMachine(new MachineConfig(3,7,2,1,100), selector, new DefaultPrizeEvaluator())
                .spin();

        assertThat(selector.lastColourCount).isEqualTo(7);
        assertThat(selector.calls).isEqualTo(3);
    }

    @Test
    void jackpot_never_credits_free_plays_even_when_float_is_small() {
        PlayResult result = machine(new MachineConfig(4, 4, 2, 5, 3), 7, 7, 7, 7).play();
        assertThat(result.prize()).isEqualTo(PrizeType.JACKPOT);
        assertThat(result.payout()).isEqualTo(3);
        assertThat(result.freePlaysCredited()).isZero();
    }
}
