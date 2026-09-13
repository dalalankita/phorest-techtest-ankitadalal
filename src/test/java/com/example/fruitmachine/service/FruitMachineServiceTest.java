package com.example.fruitmachine.service;

import com.example.fruitmachine.domain.MachineConfig;
import com.example.fruitmachine.domain.PrizeType;
import com.example.fruitmachine.domain.support.ScriptedColourSelector;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FruitMachineServiceTest {

    @Test
    void play_after_configure_uses_configured_machine() {
        FruitMachineService service = new FruitMachineService(new ScriptedColourSelector(2,2,2,2));

        service.configure(MachineConfig.classic(1,100));
        assertThat(service.play().prize()).isEqualTo(PrizeType.JACKPOT);
    }

    @Test
    void state_reflect_configured_machine() {
        FruitMachineService service = new FruitMachineService(new ScriptedColourSelector());

        service.configure(MachineConfig.classic(1,250));
        assertThat(service.state().currentFloat()).isEqualTo(250);
    }

    @Test
    void reconfigure_the_machine_state() {
        FruitMachineService service = new FruitMachineService(new ScriptedColourSelector(2,2,2,2));

        service.configure(MachineConfig.classic(1,10));
        service.play();
        service.configure(MachineConfig.classic(1,250));
        assertThat(service.state().currentFloat()).isEqualTo(250);
    }
}
