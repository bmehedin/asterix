package org.encoder.smi.controllers;

import org.encoder.smi.services.SmiDispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path="smi")
public class SmiController {

    private final SmiDispatcherService smiDispatcherService;

    @Autowired
    public SmiController(SmiDispatcherService smiDispatcherService) {
        this.smiDispatcherService = smiDispatcherService;
    }

    @PostMapping("/send-data/{nbOfPositions}/{nbOfInfringedPositions}/{xPosition}/{yPosition}/{altitude}")
    public void receiveAsterixData(
            @PathVariable int nbOfPositions,
            @PathVariable int nbOfInfringedPositions,
            @PathVariable int xPosition,
            @PathVariable int yPosition,
            @PathVariable int altitude) {

        this.smiDispatcherService.dispatch(nbOfPositions, nbOfInfringedPositions, xPosition, yPosition, altitude);
    }
}
