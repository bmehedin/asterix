package org.encoder.common;

import java.util.Set;

public class Constants {

    public static final int TIME_STEP = 4;
    public static final int TRACK_STEP = 35;
    public static final int VELOCITY_STEP = 2;
    public static final Set<String> SPLITTABLE_CELLS = Set.of("Calculated Track Position. (Cartesian)",
            "Calculated Track Velocity (Cartesian)");
    public static final Set<String> COMPUTABLE_FIELD_IDS = Set.of("I062/100", "I062/185", "I062/220");
    public static final String BINARY_CATEGORY_STRING = "00111110";
    public static final String BASE_FRN_STRING = "00000001000000010000000100000000";
//                                                01234567
    public static final String ASTERIX_EXCEL_FILE_PATH = "./data/output/asterix/asterix-data.xlsx";
    public static final String ASTERIX_RAW_FILE_PATH = "./data/output/asterix/asterix-data.raw";

    public static final Set<String> SMI_ASTERIX_IDS = Set.of("I062/010", "I062/040", "I062/060", "I062/070", "I062/080",
            "I062/100", "I062/135", "I062/185");
    public static final Set<Integer> SMI_ASTERIX_SUB_IDS = Set.of();
    public static final int SMI_INITIAL_TIMESTAMP = 98464;
    public static final double SMI_HORIZONTAL_THRESHOLD = 9260;
    public static final double SMI_VERTICAL_THRESHOLD = 304;
    public static final String SMI_EXCEL_FILE_PATH = "./data/output/smi/smi-data.xlsx";
    public static final String SMI_EXCEL_CONFLICT_FILE_PATH = "./data/output/smi/smi-conflict-data.xlsx";
    public static final String SMI_RAW_FILE_PATH = "./data/output/smi/smi-data.raw";

    public static final double EARTH_MAJOR_AXIS = 6378137.0;
    public static final double EARTH_MINOR_AXIS = 6356752.314;
    public static final double FLATTENING = (EARTH_MAJOR_AXIS - EARTH_MINOR_AXIS) / EARTH_MAJOR_AXIS;
    public static final double ECCENTRICITY_SQ = 2 * FLATTENING - Math.pow(FLATTENING, 2);
}
