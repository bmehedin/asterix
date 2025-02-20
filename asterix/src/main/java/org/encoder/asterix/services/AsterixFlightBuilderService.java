package org.encoder.asterix.services;

import org.encoder.asterix.models.FlightData;
import org.encoder.common.AsterixFlightData;
import org.encoder.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AsterixFlightBuilderService {

    private final Random rand = new Random();
    private final AsterixFlightData asterixFlightData;

    @Autowired
    public AsterixFlightBuilderService(AsterixFlightData asterixFlightData) {
        this.asterixFlightData = asterixFlightData;
    }

    public void initiateAsterixValues(int nbOfFlights) {

        for (int i = 0; i < nbOfFlights; i++) {

            FlightData flightData = new FlightData();

            asterixFlightData.getAsterixFields().forEach(asterixField -> {

                String fieldFormat = asterixField.getFormat();
                String id = asterixField.getId();
                int fieldLength = asterixField.getLength();
                int fieldValue = generateInitialFieldValue(fieldLength, id);

                if (!fieldFormat.equals("compound")) {
                    flightData.getAsterixFieldValues().put(asterixField, fieldValue);
                }
            });

            asterixFlightData.getAsterixSubfields().forEach(asterixSubfield -> {

                int subfieldValue = rand.nextInt(2);
                flightData.getAsterixSubfieldValues().put(asterixSubfield, subfieldValue);
            });

            asterixFlightData.addFlightData(flightData);
        }
    }



    private int generateInitialFieldValue(int fieldLength, String id) {

//        Item 080's binary value must never end in 1 - its field length of 1 is misinterpreted in that case
        if (id.equals("I062/080")) {
            return 0;
        }

        if (!Constants.COMPUTABLE_FIELD_IDS.contains(id)) {

            return switch (fieldLength) {
                case 1 -> rand.nextInt(200);
                case 2 -> rand.nextInt(4000);
                case 3 -> rand.nextInt(5000);
                default -> 0;
            };
        } else {

            return switch (id) {
                case "I062/100" -> rand.nextInt(20000) - 10000;
                case "I062/185" -> rand.nextInt(1000) - 500;
                case "I062/220" -> rand.nextInt(500) - 250;
                default -> 0;
            };
        }
    }
}
