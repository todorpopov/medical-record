import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import {UserAuth} from '../common/interfaces/user.auth';
import {LocalStorageService} from './local-storage.service';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
  private api: string = `${environment.apiGateway}/auth`

  constructor(
    private httpClient: HttpClient,
    private localStorageService: LocalStorageService,
  ) {}

  public logIn(userType: 'patient' | 'doctor' | 'admin', email: string, password: string): Observable<UserAuth> {
    let url: string = '';

    switch (userType) {
      case "patient": {
        url = `${this.api}/log-patient-in`;
        break;
      }
      case "doctor": {
        url = `${this.api}/log-doctor-in`;
        break;
      }
      case "admin": {
        url = `${this.api}/log-admin-in`;
        break;
      }
    }
    return this.httpClient.post<UserAuth>(url, {'email': email, 'password': password});
  }

  public logOut(): void {
    this.localStorageService.removeUserAuth();
  }
}
