package org.encoder.smi.controllers;

import org.encoder.common.Constants;
import org.encoder.common.controllers.BaseController;
import org.encoder.smi.services.SmiDispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path="smi")
public class SmiController extends BaseController {

    private final SmiDispatcherService smiDispatcherService;

    @Autowired
    public SmiController(SmiDispatcherService smiDispatcherService) {
        this.smiDispatcherService = smiDispatcherService;
    }

    @PostMapping("/{nbOfPositions}/{nbOfInfringedPositions}/{xPosition}/{yPosition}/{altitude}")
    public ResponseEntity<Resource> receiveAsterixData(
            @PathVariable int nbOfPositions,
            @PathVariable int nbOfInfringedPositions,
            @PathVariable int xPosition,
            @PathVariable int yPosition,
            @PathVariable int altitude) {

        this.smiDispatcherService.dispatch(nbOfPositions, nbOfInfringedPositions, xPosition, yPosition, altitude);
        return serveZipFile(Constants.SMI_ARCHIVE_PATH);
    }
}
