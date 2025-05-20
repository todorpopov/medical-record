import {Component} from '@angular/core';
import {Router} from '@angular/router';
import {NgIf} from '@angular/common';
import {LocalStorageService} from '../../services/local-storage.service';
import {AuthResponse} from '../../common/interfaces/auth.response';

@Component({
  selector: 'app-home',
  imports: [
    NgIf
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  userFirstName: string = '';

  constructor(
    private router: Router,
    private localStorageService: LocalStorageService,
  ) {
    const name = this.localStorageService.getUserFirstName();
    this.userFirstName = name ? name : 'User';
  }

  redirectToLogIn(): void {
    this.router.navigate(['/login'])
      .catch(err => {
        console.log(err);
      })
  }

  redirectToRegister(): void {
    this.router.navigate(['/register'])
      .catch(err => {
        console.log(err);
      })
  }

  isUserLogged() {
    return this.localStorageService.isUserLoggedIn()
  }

  isUserAdmin() {
    return this.localStorageService.getUserRole() === 'admin';
  }

  redirectToAdminDashboard() {
    this.router.navigate(['/dashboard'])
      .catch(err => {
        console.log(err);
      })
  }

  redirectToMenu() {

  }

  logOut() {
    this.localStorageService.removeUserAuth();

    this.router.navigate(['/'])
      .catch(err => {
        console.log(err);
      })
  }
}
