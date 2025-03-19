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
    styles: [``]
})
export class AppComponent {
    title = 'Medical Records App';
}
