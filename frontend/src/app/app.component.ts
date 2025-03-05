import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  template: `
    <div class="app-container">
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [`
    .app-container {
      padding: 20px;
      min-height: 100vh;
      background-color:rgb(210, 210, 210);
    }
  `]
})
export class AppComponent {
  title = 'Medical Records App';
}
