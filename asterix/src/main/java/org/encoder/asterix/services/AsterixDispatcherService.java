package org.encoder.asterix.services;

import org.encoder.common.services.BinaryEncoderService;
import org.encoder.common.AsterixFlightData;
import org.encoder.common.Constants;
import org.encoder.common.services.XMLReaderService;
import org.encoder.common.services.ZipFileBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AsterixDispatcherService {

    private final BaseAsterixService baseAsterixService;
    private final XMLReaderService xmlReaderService;
    private final AsterixFlightBuilderService asterixFlightBuilderService;
    private final AsterixExcelDataBuilderService asterixExcelDataBuilderService;
    private final BinaryEncoderService binaryEncoderService;
    private final AsterixFlightData asterixFlightData;
    private final ZipFileBuilderService zipFileBuilderService;

    @Autowired
    public AsterixDispatcherService(XMLReaderService xmlReaderService,
                                    BaseAsterixService baseAsterixService,
                                    AsterixFlightBuilderService asterixFlightBuilderService,
                                    AsterixExcelDataBuilderService asterixExcelDataBuilderService,
                                    BinaryEncoderService binaryEncoderService,
                                    AsterixFlightData asterixFlightData,
                                    ZipFileBuilderService zipFileBuilderService) {

        this.baseAsterixService = baseAsterixService;
        this.xmlReaderService = xmlReaderService;
        this.asterixFlightBuilderService = asterixFlightBuilderService;
        this.asterixExcelDataBuilderService = asterixExcelDataBuilderService;
        this.binaryEncoderService = binaryEncoderService;
        this.asterixFlightData = asterixFlightData;
        this.zipFileBuilderService = zipFileBuilderService;
    }

    public void dispatch(Set<String> asterixIds, int nbOfFlights, int nbOfPositions) {

        Set<Integer> asterixSubfieldIds = baseAsterixService.sanitiseCompoundAsterixIds(asterixIds);

        xmlReaderService.readAsterixData(asterixIds, asterixSubfieldIds);
        asterixFlightBuilderService.initiateAsterixValues(nbOfFlights);
        asterixExcelDataBuilderService.populateExcelData(nbOfPositions);
        binaryEncoderService.encode(Constants.ASTERIX_EXCEL_FILE_PATH, Constants.ASTERIX_RAW_FILE_PATH);
        asterixFlightData.clearData();
        zipFileBuilderService.zipFolder(Constants.ASTERIX_PATH, Constants.ASTERIX_ARCHIVE_PATH);

    }
}
