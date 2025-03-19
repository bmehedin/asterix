export class AsterixField {
  readonly id: string;
  readonly name: string;
  readonly isMandatory: boolean;

  private constructor(id: string, name: string, isMandatory: boolean) {
    this.id = id;
    this.name = name;
    this.isMandatory = isMandatory;
  }

  static readonly DATA_SOURCE_IDENTIFIER = new AsterixField("I062/010", "Data Source Identifier", true);
  static readonly TRACK_NUMBER = new AsterixField("I062/040", "Track Number", true);
  static readonly TRACK_MODE_3A_CODE = new AsterixField("I062/060", "Track Mode 3/A Code", false);
  static readonly TIME_OF_TRACK_INFORMATION = new AsterixField("I062/070", "Time of Track Information", true);
  static readonly TRACK_STATUS = new AsterixField("I062/080", "Track Status", true);
  static readonly CALCULATED_TRACK_POSITION = new AsterixField("I062/100", "Calculated Track Position (Cartesian)", false);
  static readonly CALCULATED_TRACK_BAROMETRIC_ALTITUDE = new AsterixField("I062/135", "Calculated Track Barometric Altitude", false);
  static readonly MEASURED_FLIGHT_LEVEL = new AsterixField("I062/136", "Measured Flight Level", false);
  static readonly CALCULATED_TRACK_VELOCITY = new AsterixField("I062/185", "Calculated Track Velocity", false);
  static readonly CALCULATED_RATE_OF_CLIMB_DESCENT = new AsterixField("I062/220", "Calculated Rate of Climb/Descent", false);

  static readonly BAROMETRIC_PRESSURE_SETTING = new AsterixField("I062/380-28", "Barometric Pressure Setting", false)
  static readonly FLIGHT_CATEGORY = new AsterixField("I062/390-4", "Flight Category", false);
  static readonly WAKE_TURBULENCE_CATEGORY = new AsterixField("I062/390-6", "Wake Turbulence Category", false);

  static values(): AsterixField[] {
    return [
      this.DATA_SOURCE_IDENTIFIER,
      this.TRACK_NUMBER,
      this.TRACK_MODE_3A_CODE,
      this.TIME_OF_TRACK_INFORMATION,
      this.TRACK_STATUS,
      this.CALCULATED_TRACK_POSITION,
      this.CALCULATED_TRACK_BAROMETRIC_ALTITUDE,
      this.MEASURED_FLIGHT_LEVEL,
      this.CALCULATED_TRACK_VELOCITY,
      this.CALCULATED_RATE_OF_CLIMB_DESCENT,
      this.BAROMETRIC_PRESSURE_SETTING,
      this.FLIGHT_CATEGORY,
      this.WAKE_TURBULENCE_CATEGORY
    ];
  }
}
