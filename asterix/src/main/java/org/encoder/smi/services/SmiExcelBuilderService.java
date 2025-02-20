package org.encoder.smi.services;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.encoder.common.Constants;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class SmiExcelBuilderService {

    public void generateFlightData(int numPositions,
                                   int numInfringedPositions,
                                   int xPosition,
                                   int yPosition,
                                   int altitude) {

        List<Integer> timestamps = new ArrayList<>();

        for (int i = 0; i < numPositions; i++) {
            timestamps.add((Constants.SMI_INITIAL_TIMESTAMP + i * Constants.TIME_STEP));
        }

        List<Double> flight1X = new ArrayList<>();
        List<Double> flight1Y = new ArrayList<>();
        List<Double> flight1Alt = new ArrayList<>();

        List<Double> flight1Vx = new ArrayList<>();
        List<Double> flight1Vy = new ArrayList<>();

        List<Double> flight1Lat = new ArrayList<>();
        List<Double> flight1Long = new ArrayList<>();
        int flight1TrackNumber = 1234;

        flight1X.add((double) xPosition);
        flight1Y.add((double) yPosition);
        flight1Alt.add((double) altitude);

        for (int i = 0; i < numPositions; i++) {
            flight1Vx.add(-300.0 + i * (600.0 / (numPositions - 1)));
            flight1Vy.add(i * (600.0 / (numPositions - 1)));
        }

        double[] coord1 = computeCoord(flight1X.get(0), flight1Y.get(0), flight1Alt.get(0), 0, 0);
        flight1Lat.add(coord1[0]);
        flight1Long.add(coord1[1]);

        double ct = Math.sqrt(2);
        List<Double> flight2X = new ArrayList<>();
        List<Double> flight2Y = new ArrayList<>();
        List<Double> flight2Alt = new ArrayList<>();
        List<Double> flight2Lat = new ArrayList<>();
        List<Double> flight2Long = new ArrayList<>();
        int flight2TrackNumber = 4321;

        flight2X.add(flight1X.get(0) + (Constants.SMI_HORIZONTAL_THRESHOLD / ct) + 10);
        flight2Y.add(flight1Y.get(0) + (Constants.SMI_HORIZONTAL_THRESHOLD / ct) + 10);
        flight2Alt.add(flight1Alt.get(0) + Constants.SMI_VERTICAL_THRESHOLD + 10);

        double[] coord2 = computeCoord(flight2X.get(0), flight2Y.get(0), flight2Alt.get(0), 0, 0);
        flight2Lat.add(coord2[0]);
        flight2Long.add(coord2[1]);

        List<Double> horizontalSep = new ArrayList<>();
        List<Double> verticalSep = new ArrayList<>();
        List<Boolean> conflict = new ArrayList<>();

        horizontalSep.add(Math.sqrt(Math.pow(flight1X.get(0) - flight2X.get(0), 2) + Math.pow(flight1Y.get(0) - flight2Y.get(0), 2)));
        verticalSep.add(Math.abs(flight1Alt.get(0) - flight2Alt.get(0)));
        conflict.add(horizontalSep.get(0) < Constants.SMI_HORIZONTAL_THRESHOLD && verticalSep.get(0) < Constants.SMI_VERTICAL_THRESHOLD);

        int conflictStartIndex = (numPositions - numInfringedPositions) / 2;
        int conflictFinishIndex = conflictStartIndex + numInfringedPositions;

        for (int i = 1; i < numPositions; i++) {
            flight1X.add(flight1X.get(i - 1) + 5 * flight1Vx.get(i - 1));
            flight1Y.add(flight1Y.get(i - 1) + 5 * flight1Vy.get(i - 1));
            flight1Alt.add(flight1Alt.get(i - 1));
            coord1 = computeCoord(flight1X.get(i), flight1Y.get(i), flight1Alt.get(i), 0, 0);
            flight1Lat.add(coord1[0]);
            flight1Long.add(coord1[1]);

            if (i >= conflictStartIndex && i < conflictFinishIndex) {
                flight2X.add(flight1X.get(i) + (Constants.SMI_HORIZONTAL_THRESHOLD / ct) - 500);
                flight2Y.add(flight1Y.get(i) + (Constants.SMI_HORIZONTAL_THRESHOLD / ct) - 500);
                flight2Alt.add(flight1Alt.get(i) + Constants.SMI_VERTICAL_THRESHOLD - 50);
            } else {
                flight2X.add(flight1X.get(i) + (Constants.SMI_HORIZONTAL_THRESHOLD / ct) + 10);
                flight2Y.add(flight1Y.get(i) + (Constants.SMI_HORIZONTAL_THRESHOLD / ct) + 10);
                flight2Alt.add(flight1Alt.get(i) + Constants.SMI_VERTICAL_THRESHOLD + 10);
            }
            coord2 = computeCoord(flight2X.get(i), flight2Y.get(i), flight2Alt.get(i), 0, 0);
            flight2Lat.add(coord2[0]);
            flight2Long.add(coord2[1]);

            horizontalSep.add(Math.sqrt(Math.pow(flight1X.get(i) - flight2X.get(i), 2) + Math.pow(flight1Y.get(i) - flight2Y.get(i), 2)));
            verticalSep.add(Math.abs(flight1Alt.get(i) - flight2Alt.get(i)));
            conflict.add(horizontalSep.get(i) < Constants.SMI_HORIZONTAL_THRESHOLD && verticalSep.get(i) < Constants.SMI_VERTICAL_THRESHOLD);
        }

        List<Map<String, Object>> flight1Data = createFlightDataFrame(flight1TrackNumber, timestamps,
                flight1X, flight1Y, flight1Alt, flight1Vx, flight1Vy);
        List<Map<String, Object>> flight2Data = createFlightDataFrame(flight2TrackNumber, timestamps,
                flight2X, flight2Y, flight2Alt, flight1Vx, flight1Vy);

        List<Map<String, Object>> flight1ConflictData = createConflictDataFrame(flight1TrackNumber, timestamps,
                flight1Lat, flight1Long, horizontalSep, verticalSep, conflict);
        List<Map<String, Object>> flight2ConflictData = createConflictDataFrame(flight2TrackNumber, timestamps,
                flight2Lat, flight2Long, horizontalSep, verticalSep, conflict);

        List<Map<String, Object>> allFlightData = new ArrayList<>();
        allFlightData.addAll(flight1Data);
        allFlightData.addAll(flight2Data);

        List<Map<String, Object>> allConflictFlightData = new ArrayList<>();
        allConflictFlightData.addAll(flight1ConflictData);
        allConflictFlightData.addAll(flight2ConflictData);

        applyConversionToAsterixUnits(allFlightData);

        writeDataToExcel(allFlightData, Constants.SMI_EXCEL_FILE_PATH);
        writeDataToExcel(allConflictFlightData, Constants.SMI_EXCEL_CONFLICT_FILE_PATH);
    }

    private double[] computeCoord(double x, double y, double alt, double mrtLat, double mrtLong) {

        double lat0 = Math.toRadians(mrtLat);
        double lon0 = Math.toRadians(mrtLong);

        double N = Constants.EARTH_MAJOR_AXIS / Math.sqrt(1 - Constants.ECCENTRICITY_SQ * Math.pow(Math.sin(lat0), 2));
        double X0_ecef = (N) * Math.cos(lat0) * Math.cos(lon0);
        double Y0_ecef = (N) * Math.cos(lat0) * Math.sin(lon0);
        double Z0_ecef = (N * (1 - Constants.ECCENTRICITY_SQ)) * Math.sin(lat0);

        double dx = Math.cos(lon0) * x - Math.sin(lon0) * y;
        double dy = Math.sin(lon0) * x + Math.cos(lon0) * y;
        double dz = Math.sin(lat0) * alt + Math.cos(lat0) * x;

        double X_ecef = X0_ecef + dx;
        double Y_ecef = Y0_ecef + dy;
        double Z_ecef = Z0_ecef + dz;

        double p = Math.sqrt(X_ecef * X_ecef + Y_ecef * Y_ecef);
        double theta = Math.atan2(Z_ecef * Constants.EARTH_MAJOR_AXIS, p * Constants.EARTH_MINOR_AXIS);
        double latRad = Math.atan2(Z_ecef + Math.pow(Constants.EARTH_MAJOR_AXIS, 2) / Constants.EARTH_MINOR_AXIS * Math.pow(Math.sin(theta), 3),
                p - Constants.ECCENTRICITY_SQ * Constants.EARTH_MAJOR_AXIS * Math.pow(Math.cos(theta), 3));
        double lonRad = Math.atan2(Y_ecef, X_ecef);

        return new double[]{Math.toDegrees(latRad), Math.toDegrees(lonRad)};
    }

    private List<Map<String, Object>> createFlightDataFrame(int trackNumber,
                                                            List<Integer> timestamps,
                                                            List<Double> x,
                                                            List<Double> y,
                                                            List<Double> alt,
                                                            List<Double> vx, List<Double> vy) {

        List<Map<String, Object>> flightData = new ArrayList<>();

        for (int i = 0; i < timestamps.size(); i++) {

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("Track Number", trackNumber);
            row.put("Data Source Identifier", 0);
            row.put("Track Mode-3/A Code", 1);
            row.put("Time of Track Information", timestamps.get(i));
            row.put("Track Status", 0);
            row.put("Calculated Track Position. (Cartesian) X", x.get(i));
            row.put("Calculated Track Position. (Cartesian) Y", y.get(i));
            row.put("Calculated Track Barometric Altitude", alt.get(i));
            row.put("Calculated Track Velocity (Cartesian) X", vx.get(i));
            row.put("Calculated Track Velocity (Cartesian) Y", vy.get(i));
            flightData.add(row);
        }
        return flightData;
    }

    private List<Map<String, Object>> createConflictDataFrame(int trackNumber,
                                                              List<Integer> timestamps,
                                                              List<Double> lat,
                                                              List<Double> longitude,
                                                              List<Double> horizontalSep,
                                                              List<Double> verticalSep,
                                                              List<Boolean> conflict) {

        List<Map<String, Object>> conflictData = new ArrayList<>();
        for (int i = 0; i < timestamps.size(); i++) {

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("Track Number", trackNumber);
            row.put("Time of Track Information", timestamps.get(i));
            row.put("Latitude", lat.get(i));
            row.put("Longitude", longitude.get(i));
            row.put("Horizontal Separation", horizontalSep.get(i));
            row.put("Vertical Separation", verticalSep.get(i));
            row.put("Is Conflict Position", conflict.get(i));
            conflictData.add(row);
        }
        return conflictData;
    }

    private void applyConversionToAsterixUnits(List<Map<String, Object>> data) {

        for (Map<String, Object> row : data) {

            row.put("Calculated Track Barometric Altitude", ((double) row.get("Calculated Track Barometric Altitude") / 100) * 4);
            row.put("Calculated Track Position. (Cartesian) X", (double) row.get("Calculated Track Position. (Cartesian) X") * 2);
            row.put("Calculated Track Position. (Cartesian) Y", (double) row.get("Calculated Track Position. (Cartesian) Y") * 2);
            row.put("Calculated Track Velocity (Cartesian) X", (double) row.get("Calculated Track Velocity (Cartesian) X") * 4);
            row.put("Calculated Track Velocity (Cartesian) Y", (double) row.get("Calculated Track Velocity (Cartesian) Y") * 4);
        }
    }

    private void writeDataToExcel(List<Map<String, Object>> data, String fileName) {

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             FileOutputStream fileOut = new FileOutputStream(fileName)) {

            XSSFSheet sheet = workbook.createSheet("Data");

            XSSFRow headerRow = sheet.createRow((short)0);
            Set<String> headers = data.get(0).keySet();
            int colIndex = 0;
            for (String header : headers) {
                Cell cell = headerRow.createCell(colIndex++);
                cell.setCellValue(header);
            }

            int rowIndex = 1;
            for (Map<String, Object> row : data) {
                XSSFRow excelRow = sheet.createRow(rowIndex++);
                colIndex = 0;
                for (String header : headers) {
                    Cell cell = excelRow.createCell(colIndex++);
                    Object value = row.get(header);
                    if (value instanceof Double) {
                        cell.setCellValue((Double) value);
                    } else if (value instanceof Integer) {
                        cell.setCellValue((Integer) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(fileOut);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
