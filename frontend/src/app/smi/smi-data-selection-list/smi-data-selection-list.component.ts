import { Component } from '@angular/core';
import {MatButton} from '@angular/material/button';
import {FormsModule} from '@angular/forms';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {take} from 'rxjs';
import {SmiService} from '../smi.service';

@Component({
  standalone: true,
  selector: 'app-smi-data-selection-list',
  imports: [
    MatButton,
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel
  ],
  templateUrl: './smi-data-selection-list.component.html',
  styleUrl: './smi-data-selection-list.component.css'
})
export class SmiDataSelectionListComponent {

  nbOfPositions = 0;
  nbOfInfringedPositions = 0;
  xPosition: number = 0;
  yPosition: number = 0;
  altitude: number = 0;

  constructor(private smiService: SmiService) {
  }

  onSubmit() {

    this.smiService.sendSmiData(this.nbOfPositions, this.nbOfInfringedPositions, this.xPosition, this.yPosition, this.altitude)
      .pipe(take(1))
      .subscribe({
        next: () => console.log("Request sent successfully!"),
        error: (err) => console.error("Error sending request:", err)
      });
  }
}
