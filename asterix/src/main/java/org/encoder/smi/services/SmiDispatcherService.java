package org.encoder.smi.services;

import org.encoder.common.AsterixFlightData;
import org.encoder.common.services.BinaryEncoderService;
import org.encoder.common.services.XMLReaderService;
import org.encoder.common.Constants;
import org.encoder.common.services.ZipFileBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SmiDispatcherService {

    private final XMLReaderService xmlReaderService;
    private final SmiExcelBuilderService smiExcelBuilderService;
    private final BinaryEncoderService binaryEncoderService;
    private final AsterixFlightData asterixFlightData;
    private final ZipFileBuilderService zipFileBuilderService;

    @Autowired
    public SmiDispatcherService(XMLReaderService xmlReaderService,
                                SmiExcelBuilderService smiExcelBuilderService,
                                BinaryEncoderService binaryEncoderService,
                                AsterixFlightData asterixFlightData,
                                ZipFileBuilderService zipFileBuilderService) {
        this.xmlReaderService = xmlReaderService;
        this.smiExcelBuilderService = smiExcelBuilderService;
        this.binaryEncoderService = binaryEncoderService;
        this.asterixFlightData = asterixFlightData;
        this.zipFileBuilderService = zipFileBuilderService;
    }

    public void dispatch(int nbOfPositions,
                         int nbOfInfringedPositions,
                         int xPosition,
                         int yPosition,
                         int altitude) {

        xmlReaderService.readAsterixData(Constants.SMI_ASTERIX_IDS, Constants.SMI_ASTERIX_SUB_IDS);
        smiExcelBuilderService.generateFlightData(nbOfPositions, nbOfInfringedPositions, xPosition, yPosition, altitude);
        binaryEncoderService.encode(Constants.SMI_EXCEL_FILE_PATH, Constants.SMI_RAW_FILE_PATH);
        asterixFlightData.clearData();
        zipFileBuilderService.zipFolder(Constants.SMI_PATH, Constants.SMI_ARCHIVE_PATH);
    }
}
