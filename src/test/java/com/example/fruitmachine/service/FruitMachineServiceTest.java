package com.example.fruitmachine.service;

import com.example.fruitmachine.domain.ColourSelector;
import com.example.fruitmachine.domain.MachineConfig;
import com.example.fruitmachine.domain.PrizeType;
import com.example.fruitmachine.domain.support.ScriptedColourSelector;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    void concurrent_plays_settle_the_float_consistently() throws InterruptedException {
        ColourSelector fixed = new ColourSelector() {
            private int i = 0;
            private final int[] pattern = {0, 0, 1};
            @Override public int nextColour(int colourCount) {
                return pattern[i++ % pattern.length];
            }
        };

        long start = 1_000_000;
        long cost  = 1;
        FruitMachineService service = new FruitMachineService(fixed);
        service.configure(new MachineConfig(3, 2, 2, cost, start));

        int threads = 50, playsPerThread = 200;
        int totalPlays = threads * playsPerThread;

        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch go = new CountDownLatch(1);
        for (int t = 0; t < threads; t++) {
            pool.submit(() -> {
                try { go.await(); } catch (InterruptedException ignored) {}
                for (int p = 0; p < playsPerThread; p++) service.play();
            });
        }
        go.countDown();
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);

        long expected = start - (long) totalPlays * 5;
        assertThat(service.state().currentFloat()).isEqualTo(expected);
    }
}
