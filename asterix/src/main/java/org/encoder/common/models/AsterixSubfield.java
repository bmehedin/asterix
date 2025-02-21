package org.encoder.common.models;

import lombok.Getter;

@Getter
public class AsterixSubfield {
    private final String parentId;
    private final int frn;
    private final int length;
    private final String name;
    private final String format;

    public AsterixSubfield(String parentId, int frn, int length, String name, String format) {
        this.parentId = parentId;
        this.frn = frn;
        this.length = length;
        this.name = name;
        this.format = format;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AsterixSubfield other = (AsterixSubfield) obj;
        return name != null && name.equals(other.getName());
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
