import {Component} from '@angular/core';
import {MatButton} from '@angular/material/button';
import {FormsModule} from '@angular/forms';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatInput} from '@angular/material/input';
import {take} from 'rxjs';
import {SmiService} from '../smi.service';
import {NgIf} from '@angular/common';
import {MatProgressSpinner} from '@angular/material/progress-spinner';

@Component({
  standalone: true,
  selector: 'app-smi-data-selection-list',
  imports: [
    MatButton,
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    NgIf,
    MatProgressSpinner
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

  zipFileUrl: string | null = null;
  isLoading: boolean = false;

  constructor(private smiService: SmiService) {
  }

  onSubmit() {

    this.isLoading = true;

    this.smiService.sendSmiData(this.nbOfPositions, this.nbOfInfringedPositions, this.xPosition, this.yPosition, this.altitude)
      .pipe(take(1))
      .subscribe({
        next: (blob) => {

          this.zipFileUrl = window.URL.createObjectURL(blob);
          this.isLoading = false;
        },
        error: (err) => {

          console.error("Error receiving ZIP file:", err);
          this.zipFileUrl = null;
          this.isLoading = false;
        }
      });
  }

  downloadZip() {

    if (this.zipFileUrl) {
      const link = document.createElement('a');
      link.href = this.zipFileUrl;
      link.download = 'smi_data.zip';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      this.zipFileUrl = null;
    }
  }
}
