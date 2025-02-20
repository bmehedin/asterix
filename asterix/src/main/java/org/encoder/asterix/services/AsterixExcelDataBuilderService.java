package org.encoder.asterix.services;

import  java.io.*;
import java.util.HashSet;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.encoder.asterix.models.AsterixField;
import org.encoder.asterix.models.AsterixSubfield;
import org.encoder.asterix.models.FlightData;
import org.encoder.common.AsterixFlightData;
import org.encoder.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AsterixExcelDataBuilderService {

    private final AsterixFlightData asterixFlightData;

    @Autowired
    public AsterixExcelDataBuilderService(AsterixFlightData asterixFlightData) {
        this.asterixFlightData = asterixFlightData;
    }

    public void populateExcelData(int nbOfPositions) {

        try {

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Data");
            XSSFRow rowhead = sheet.createRow((short)0);

            int cellCounter = addHeaderRow(asterixFlightData.getAsterixFields(), rowhead, 0);
            addHeaderRow(asterixFlightData.getAsterixSubfields(), rowhead, cellCounter);

            int rowCounter = 1;

            for (FlightData flightData : asterixFlightData.getFlightDataSet()) {
                for (int position = 0; position < nbOfPositions; position++) {
                    XSSFRow row = sheet.createRow(rowCounter++);
                    fillRowWithFlightData(row, flightData, position);
                }
            }

            FileOutputStream fileOut = new FileOutputStream(Constants.ASTERIX_EXCEL_FILE_PATH);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int addHeaderRow(HashSet<? extends AsterixSubfield> fields,
                                     XSSFRow rowhead,
                                     int cellCounter) {

        for (AsterixSubfield field : fields) {

            if (!field.getFormat().equals("compound")) {

                String fieldName = field.getName();
                if (Constants.SPLITTABLE_CELLS.contains(fieldName)) {

                    rowhead.createCell(cellCounter).setCellValue(fieldName + " X");
                    cellCounter++;
                    rowhead.createCell(cellCounter).setCellValue(fieldName + " Y");
                } else {
                    rowhead.createCell(cellCounter).setCellValue(fieldName);
                }

                cellCounter++;
            }
        }

        return cellCounter;
    }

    private void fillRowWithFlightData(XSSFRow row, FlightData flightData, int position) {
        int cellCounter = 0;

        for (AsterixField field : asterixFlightData.getAsterixFields()) {

            if (!field.getFormat().equals("compound")) {

                int value = flightData.getAsterixFieldValues().get(field);

                if (Constants.SPLITTABLE_CELLS.contains(field.getName())) {

                    int assignableValue = switch(field.getName()) {
                        case "Calculated Track Position. (Cartesian)" ->
                                value + position * Constants.TRACK_STEP;
                        case "Calculated Track Velocity (Cartesian)" ->
                                Math.min(550, value + position * Constants.VELOCITY_STEP);
                        default -> value;
                    };

                    row.createCell(cellCounter).setCellValue(assignableValue);
                    cellCounter++;
                    row.createCell(cellCounter++).setCellValue(-assignableValue);

                } else if (field.getName().equals("Time of Track Information")) {

                    int assignableValue = value + position * Constants.TIME_STEP;
                    row.createCell(cellCounter).setCellValue(assignableValue);
                    cellCounter++;
                } else {

                    row.createCell(cellCounter).setCellValue(value);
                    cellCounter++;
                }
            }
        }

        for (AsterixSubfield subfield : asterixFlightData.getAsterixSubfields()) {

            Integer value = flightData.getAsterixSubfieldValues().get(subfield);
            row.createCell(cellCounter).setCellValue(value);
            cellCounter++;
        }
    }
}
