import { Component } from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {FieldSelectionListComponent} from './asterix/field-selection-list/field-selection-list.component';
import {SmiDataSelectionListComponent} from './smi/smi-data-selection-list/smi-data-selection-list.component';
import {MatTab, MatTabGroup, MatTabLink, MatTabNav, MatTabNavPanel} from '@angular/material/tabs';

@Component({
  selector: 'app-root',
  imports: [FieldSelectionListComponent, SmiDataSelectionListComponent, MatTabGroup, MatTab, RouterLink, RouterOutlet, MatTabNav, RouterLinkActive, MatTabLink, MatTabNavPanel],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
}
