package org.encoder.common;

import lombok.Getter;
import org.encoder.common.models.AsterixField;
import org.encoder.common.models.AsterixSubfield;
import org.encoder.common.models.FlightData;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Getter
@Component
public class AsterixFlightData {

    private final HashSet<AsterixField> asterixFields = new HashSet<>();
    private final HashSet<AsterixSubfield> asterixSubfields = new HashSet<>();
    private final HashSet<FlightData> flightDataSet = new HashSet<>();

    public void addAsterixField(int frn, int length, String name, String id, String format) {

        asterixFields.add(new AsterixField(frn, length, name, id, format));
    }

    public void addAsterixSubfield(String parentId, int frn, int length, String name, String format) {

        asterixSubfields.add(new AsterixSubfield(parentId, frn, length, name, format));
    }

    public void addFlightData(FlightData flightData) {

        flightDataSet.add(flightData);
    }

    public void clearData() {

        asterixFields.clear();
        asterixSubfields.clear();
        flightDataSet.clear();
    }
}



