import { Component } from '@angular/core';
import {RouterLink} from '@angular/router';
import {Page} from '../../common/util/util';
import {AuthService} from '../../services/auth.service';
import {NgIf} from '@angular/common';
import {LocalStorageService} from '../../services/local-storage.service';
import {PatientMenuComponent} from '../../components/patient-menu/patient-menu.component';
import {DoctorMenuComponent} from '../../components/doctor-menu/doctor-menu.component';

@Component({
  selector: 'app-menu',
  imports: [
    RouterLink,
    NgIf,
    PatientMenuComponent,
    DoctorMenuComponent
  ],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent {
  private readonly page: Page = 'menu';

  constructor(
    private authService: AuthService,
    private localStorageService: LocalStorageService,
  ) {
    this.authService.fetchPages(this.page);
  }

  protected getUserRole() {
    return this.localStorageService.getUserRole();
  }
}
