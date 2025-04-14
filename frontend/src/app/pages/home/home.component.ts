import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  constructor(private router: Router) {}

  redirectToLogIn(): void {
    this.router.navigate(['/login'])
      .catch(err => {
        console.log(err)
      })
  }

  redirectToRegister(): void {
    this.router.navigate(['/register'])
      .catch(err => {
        console.log(err)
      })
  }
}
