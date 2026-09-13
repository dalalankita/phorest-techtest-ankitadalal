package com.example.fruitmachine.service;

import com.example.fruitmachine.domain.ColourSelector;
import com.example.fruitmachine.domain.DefaultPrizeEvaluator;
import com.example.fruitmachine.domain.FruitMachine;
import com.example.fruitmachine.domain.MachineConfig;
import com.example.fruitmachine.domain.PlayResult;
import org.springframework.stereotype.Service;

/**
 * Looks after the one fruit machine: keeps it in memory between requests, runs plays on it, and
 * replaces it with a fresh machine when reconfigured. Synchronized so two requests at the same
 * time take turns rather than scrambling the shared money and free-play counts.
 */
@Service
public class FruitMachineService {

    private final ColourSelector colourSelector;

    private FruitMachine machine;

    public FruitMachineService(ColourSelector colourSelector) {
        this.colourSelector = colourSelector;
    }

    public synchronized FruitMachine configure(MachineConfig config) {
        this.machine = new FruitMachine(config, colourSelector, new DefaultPrizeEvaluator());
        return this.machine;
    }

    public synchronized PlayResult play() {
        return requireMachine().play();
    }

    public synchronized FruitMachine state() {
        return requireMachine();
    }

    private FruitMachine requireMachine() {
        if (machine == null) {
            throw  new MachineNotConfiguredException();
        }
        return machine;
    }
}