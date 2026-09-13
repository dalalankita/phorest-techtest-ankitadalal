package com.example.fruitmachine.domain.support;

import com.example.fruitmachine.domain.InvalidMachineConfigException;
import com.example.fruitmachine.domain.MachineConfig;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MachineConfigTest {

    @Test
    void accepts_a_valid_config() {
        assertThatCode(() -> new MachineConfig(4, 4, 2, 1, 100)).doesNotThrowAnyException();
    }

    @Test
    void rejects_slot_count_below_three() {
        assertThatThrownBy(() -> new MachineConfig(2, 4, 2, 1, 100))
                .isInstanceOf(InvalidMachineConfigException.class);
    }

    @Test
    void rejects_k_below_two() {
        assertThatThrownBy(() -> new MachineConfig(4, 4, 1, 1, 100))
                .isInstanceOf(InvalidMachineConfigException.class);
    }

    @Test
    void rejects_k_not_less_than_slot_count() {
        assertThatThrownBy(() -> new MachineConfig(4, 4, 5, 1, 100))   // k == slotCount, invalid under your rule
                .isInstanceOf(InvalidMachineConfigException.class);
    }

    @Test
    void rejects_negative_starting_float() {
        assertThatThrownBy(() -> new MachineConfig(4, 4, 2, 1, -1))
                .isInstanceOf(InvalidMachineConfigException.class);
    }
}