# ASTERIX & SMI Flight Data Simulator

This tool simulates and generates **synthetic flight surveillance data** in the **ASTERIX CAT062** format. It produces realistic randomized flight data, outputs the results to **Excel files**, and encodes ASTERIX data into **binary-compliant ASTERIX streams**.

---

## Key Features

-  **Excel Export**: Viewable and editable output for both ASTERIX and SMI generated data
-  **Random Flight Data Generation**: Field-specific randomization with meaningful values
-  **ASTERIX CAT062 Binary Encoding**: Conforms to [EUROCONTROL]([https://www.eurocontrol.int/asterix](https://www.eurocontrol.int/publication/cat062-eurocontrol-specification-surveillance-data-exchange-asterix-part-9-category-062)) ASTERIX standards

---

## ASTERIX CAT062
The standard protocol for **System Track Messages** in ATC. Encoded fields include:

- Track Number (I062/040)
- Track Status (I062/080)
- Calculated Track Position (Cartesian) (I062/100)
- Calculated Track Barometric Altitude (I062/135)
- Calculated Track Velocity (Cartesian) (I062/185)

### SMI Format
SMI fields are simulated using domain-relevant definitions, built into:
- `SmiExcelBuilderService`
- `SmiDispatcherService`
- `SmiController`

These modules provide Excel-based SMI data generation via a dedicated REST path.

---

## System Architecture

| Module | Description |
|--------|-------------|
| `AsterixFlightBuilderService` | Builds randomized CAT062 `FlightData` |
| `AsterixExcelDataBuilderService` | Writes generated data to Excel |
| `BinaryEncoderService` | Encodes to ASTERIX binary format |
| `AsterixDispatcherService` and `SmiDispatcherService` | 	Act as orchestrators — initializing and coordinating flight generation, Excel export, and encoding via the builder and encoder services |
| `XMLReaderService` | Loads field definitions from `cat62.xml` |
| `SmiDispatcherService` | Generates and exports SMI-format Excel data |
| `AsterixController`, `SmiController` | REST endpoints for both formats |
| `Constants.java` | Global system-level constants |

---

## Usage

### Docker

```bash
docker compose up
