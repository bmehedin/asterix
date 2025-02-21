import {Component} from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {MatTabLink, MatTabNav, MatTabNavPanel} from '@angular/material/tabs';

@Component({
  selector: 'app-root',
  imports: [RouterLink, RouterOutlet, MatTabNav, RouterLinkActive, MatTabLink, MatTabNavPanel],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
}
