import { Routes } from '@angular/router';
import {FieldSelectionListComponent} from './asterix/field-selection-list/field-selection-list.component';
import {SmiDataSelectionListComponent} from './smi/smi-data-selection-list/smi-data-selection-list.component';

export const routes: Routes = [
  {path: 'asterix', component: FieldSelectionListComponent},
  {path: 'smi', component: SmiDataSelectionListComponent}
];
