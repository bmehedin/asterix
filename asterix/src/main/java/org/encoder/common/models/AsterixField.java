package org.encoder.common.models;

import lombok.Getter;

@Getter
public class AsterixField extends AsterixSubfield {

    private final String id;

    public AsterixField(int frn, int length, String name, String id, String format) {
        super(null, frn, length, name, format);
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
