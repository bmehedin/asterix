package org.encoder.common.models;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class FlightData {

    private HashMap<AsterixField, Integer> asterixFieldValues;
    private HashMap<AsterixSubfield, Integer> asterixSubfieldValues;

    public FlightData() {
        this.asterixFieldValues = new HashMap<>();
        this.asterixSubfieldValues = new HashMap<>();
    }
}
