package com.example.fruitmachine.web;

import com.example.fruitmachine.service.FruitMachineService;
import com.example.fruitmachine.web.dto.ConfigureMachineRequest;
import com.example.fruitmachine.web.dto.MachineStateResponse;
import com.example.fruitmachine.web.dto.PlayResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP surface for the machine. Thin on purpose: validate input, delegate to the service, map
 * the result to a DTO. No game logic here.
 */
@RestController
@RequestMapping("/fruit-machine")
public class FruitMachineController {

    private final FruitMachineService machineService;

    public FruitMachineController(FruitMachineService machineService) {
        this.machineService = machineService;
    }

    /** Configure (or reconfigure) the machine. */
    @PostMapping
    public ResponseEntity<MachineStateResponse> configure(@Valid @RequestBody ConfigureMachineRequest request) {
        var machine = machineService.configure(request.toDomain());
        return ResponseEntity.status(HttpStatus.CREATED).body(MachineStateResponse.from(machine));
    }

    /** Read the current machine state. */
    @GetMapping
    public MachineStateResponse state() {
        return MachineStateResponse.from(machineService.state());
    }

    /** Play one round. */
    @PostMapping("/play")
    public PlayResponse play() {
        return PlayResponse.from(machineService.play());
    }
}