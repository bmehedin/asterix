package org.encoder.common.services;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.encoder.common.models.AsterixField;
import org.encoder.common.models.AsterixSubfield;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.encoder.common.AsterixFlightData;
import org.encoder.common.Constants;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class BinaryEncoderService {

    private final AsterixFlightData asterixFlightData;

    public BinaryEncoderService(AsterixFlightData asterixFlightData) {
        this.asterixFlightData = asterixFlightData;
    }

    public void encode(String excelPath, String rawPath) {

        try (FileInputStream fis = new FileInputStream(excelPath);
             Workbook workbook = new XSSFWorkbook(fis);
             FileOutputStream fos = new FileOutputStream(rawPath)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            Map<String, Integer> columnIndexMap = getColumnIndexMap(sheet.getRow(0));
            if (rowIterator.hasNext()) rowIterator.next();

            List<AsterixField> sortedFields = sortAsterixFieldsByFrn();
            List<AsterixSubfield> sortedSubfields = sortAsterixSubfieldsByFrn();

            while (rowIterator.hasNext()) {

                Row row = rowIterator.next();

                StringBuilder finalAsterixString = new StringBuilder();
                finalAsterixString.append(determineMessageHeader());

                iterateThroughFields(sortedFields, sortedSubfields, row, columnIndexMap, finalAsterixString);

                byte[] binaryData = binaryStringToByteArray(finalAsterixString.toString());
                fos.write(binaryData);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private StringBuilder determineMessageHeader() {

        StringBuilder binaryHeaderMessage = new StringBuilder();
        binaryHeaderMessage.append(Constants.BINARY_CATEGORY_STRING);
        binaryHeaderMessage.append(determineMessageLength());
        binaryHeaderMessage.append(generateFspecString());

        return binaryHeaderMessage;
    }

    private String determineMessageLength() {

        int messageLength = 7;
        for (AsterixField field : asterixFlightData.getAsterixFields()) {

//            TODO: Fix issue when subfields from both I380 and I390 are encoded
            if (field.getId().equals("I062/380")) {
                messageLength += field.getLength() * 4;
            } else {
                messageLength += field.getLength();
            }
        }

        for (AsterixSubfield subfield : asterixFlightData.getAsterixSubfields()) {

            messageLength += subfield.getLength();
        }

        return decimalToBinary(16, messageLength);
    }

    private StringBuilder generateFspecString() {

        StringBuilder fspecString = new StringBuilder(Constants.BASE_FRN_STRING);

        for (AsterixField field : asterixFlightData.getAsterixFields()) {

            int frn = field.getFrn();
            int adjustedIndex = calculateFspecIndex(frn);

            if (adjustedIndex < fspecString.length()) {

                fspecString.setCharAt(adjustedIndex - 1, '1');
            }
        }

        return fspecString;
    }

    private int calculateFspecIndex(int frn) {

        return (frn % 7 == 0) ? frn + (frn / 7) - 1 : frn + (frn / 7);
    }

    private StringBuilder generateSubfieldFspec(AsterixField parentField) {

//        hardcoding exception for item 380 BPS
        if (parentField.getId().equals("I062/380")) {
            return new StringBuilder(Constants.BASE_380_FRN_STRING);
        }

        StringBuilder subfieldFspec = new StringBuilder("00000000");

        for (AsterixSubfield subfield : asterixFlightData.getAsterixSubfields()) {

            if (parentField.getId().equals(subfield.getParentId())) {

                int frn = subfield.getFrn();
                int adjustedIndex = calculateFspecIndex(frn);

                if (adjustedIndex < subfieldFspec.length()) {

                    subfieldFspec.setCharAt(adjustedIndex - 1, '1');
                }
            }
        }

        return subfieldFspec;
    }

    private void iterateThroughFields(List<AsterixField> sortedFields,
                                      List<AsterixSubfield> sortedSubfields,
                                      Row row,
                                      Map<String, Integer> columnIndexMap,
                                      StringBuilder finalAsterixString) {

        for (AsterixField field : sortedFields) {

            if (field.getFormat().equals("compound")) {

                finalAsterixString.append(generateSubfieldFspec(field));
                appendSubfieldData(finalAsterixString, row, columnIndexMap, sortedSubfields);

            } else {

                appendFieldData(finalAsterixString, row, field, columnIndexMap);
            }
        }
    }

    private List<AsterixField> sortAsterixFieldsByFrn() {

        List<AsterixField> sortedFields = new ArrayList<>(asterixFlightData.getAsterixFields());
        sortedFields.sort(Comparator.comparingInt(AsterixField::getFrn));
        return sortedFields;
    }

    private List<AsterixSubfield> sortAsterixSubfieldsByFrn() {

        List<AsterixSubfield> sortedSubfields = new ArrayList<>(asterixFlightData.getAsterixSubfields());
        sortedSubfields.sort(Comparator.comparingInt(AsterixSubfield::getFrn));
        return sortedSubfields;
    }

    private void appendFieldData(StringBuilder binaryMessage,
                                 Row row,
                                 AsterixField field,
                                 Map<String, Integer> columnIndexMap) {

        if (Constants.SPLITTABLE_CELLS.contains(field.getName())) {

            String xColumnName = field.getName() + " X";
            String yColumnName = field.getName() + " Y";
            int valueX = (int) getCellValueByColumnName(row, columnIndexMap, xColumnName);
            int valueY = (int) getCellValueByColumnName(row, columnIndexMap, yColumnName);
            binaryMessage.append(decimalToBinary(field.getLength() * 4, valueX));
            binaryMessage.append(decimalToBinary(field.getLength() * 4, valueY));

        } else {

            int fieldValue = (int) getCellValueByColumnName(row, columnIndexMap, field.getName());
            binaryMessage.append(decimalToBinary(field.getLength() * 8, fieldValue));
        }
    }

    private void appendSubfieldData(StringBuilder binaryMessage,
                                    Row row,
                                    Map<String, Integer> columnIndexMap,
                                    List<AsterixSubfield> sortedSubfields) {

        for (AsterixSubfield subfield : sortedSubfields) {

            int fieldValue = (int) getCellValueByColumnName(row, columnIndexMap, subfield.getName());
            binaryMessage.append(decimalToBinary(subfield.getLength() * 8, fieldValue));
        }
    }

    private Map<String, Integer> getColumnIndexMap(Row headerRow) {

        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (Cell cell : headerRow) {
            columnIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }


        return columnIndexMap;
    }

    private double getCellValueByColumnName(Row row, Map<String, Integer> columnIndexMap, String columnName) {

        Integer columnIndex = columnIndexMap.get(columnName);
        return row.getCell(columnIndex).getNumericCellValue();
    }

    public static String decimalToBinary(int length, int value) {

        String binaryString;
        if (value >= 0) {
            binaryString = Integer.toBinaryString(value);
        } else {
            binaryString = Integer.toBinaryString((1 << length) + value);
        }
        return String.format("%" + length + "s", binaryString).replace(' ', '0');
    }

    public byte[] binaryStringToByteArray(String binaryString) {
        int length = binaryString.length();
        int byteCount = length / 8;
        byte[] byteArray = new byte[byteCount];

        for (int i = 0; i < byteCount; i++) {
            String byteSegment = binaryString.substring(i * 8, (i + 1) * 8);
            byteArray[i] = (byte) Integer.parseInt(byteSegment, 2);
        }

        return byteArray;
    }

}
