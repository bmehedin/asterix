import {Component} from '@angular/core';
import {MatCheckbox} from '@angular/material/checkbox';
import {FormsModule} from '@angular/forms';
import {MatButton} from '@angular/material/button';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import {AsterixField} from '../models/asterix-field';
import {AsterixService} from '../asterix.service';
import {take} from 'rxjs';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';

@Component({
  standalone: true,
  selector: 'app-field-selection-list',
  imports: [
    MatCheckbox,
    MatButton,
    FormsModule,
    NgForOf,
    MatFormField,
    MatInput,
    MatLabel,
    NgClass,
    NgIf
  ],
  templateUrl: './field-selection-list.component.html',
  styleUrl: './field-selection-list.component.css'
})
export class FieldSelectionListComponent {

  constructor(private asterixService: AsterixService) {
  }

  asterixFields = AsterixField.values();

  numFlights: number = 0;
  numPositions: number = 0;
  selectedFields: Set<string> = new Set();
  mandatoryFields = new Set(["I062/010", "I062/040", "I062/070", "I062/080"]);

  toggleField(field: AsterixField, isChecked: boolean) {

    if (isChecked) {
      this.selectedFields.add(field.id);
    } else {
      this.selectedFields.delete(field.id);
    }
  }

  onSubmit() {

    this.asterixService.sendAsterixData(this.selectedFields, this.numFlights, this.numPositions)
      .pipe(take(1))
      .subscribe({
        next: () => console.log("Request sent successfully!"),
        error: (err) => console.error("Error sending request:", err)
      });
  }

  checkMandatoryFields(): boolean {
    return Array.from(this.mandatoryFields).every(mandatoryField =>
      this.selectedFields.has(mandatoryField)
    );
  }
}
