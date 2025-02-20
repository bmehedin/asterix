package org.encoder.asterix.controllers;

import org.encoder.asterix.services.AsterixDispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path="asterix")
public class BaseAsterixController {

    private final AsterixDispatcherService asterixDispatcherService;

    @Autowired
    public BaseAsterixController(AsterixDispatcherService asterixDispatcherService) {
        this.asterixDispatcherService = asterixDispatcherService;
    }

    @PostMapping("/send-data/{nbOfFlights}/{nbOfPositions}")
    public void receiveAsterixData(
            @RequestBody Set<String> fieldIds,
            @PathVariable int nbOfFlights,
            @PathVariable int nbOfPositions) {

        this.asterixDispatcherService.dispatch(fieldIds, nbOfFlights, nbOfPositions);
    }


}
