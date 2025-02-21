package org.encoder.asterix.controllers;

import org.encoder.asterix.services.AsterixDispatcherService;
import org.encoder.common.Constants;
import org.encoder.common.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "http://localhost:80")
@RestController
@RequestMapping(path="asterix")
public class AsterixController extends BaseController {

    private final AsterixDispatcherService asterixDispatcherService;

    @Autowired
    public AsterixController(AsterixDispatcherService asterixDispatcherService) {
        this.asterixDispatcherService = asterixDispatcherService;
    }

    @PostMapping("/{nbOfFlights}/{nbOfPositions}")
    public ResponseEntity<Resource> receiveAsterixData(
            @RequestBody Set<String> fieldIds,
            @PathVariable int nbOfFlights,
            @PathVariable int nbOfPositions) {

        this.asterixDispatcherService.dispatch(fieldIds, nbOfFlights, nbOfPositions);
        return serveZipFile(Constants.ASTERIX_ARCHIVE_PATH);
    }
}
