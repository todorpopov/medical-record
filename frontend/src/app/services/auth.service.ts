import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {AuthResponse} from '../common/interfaces/auth.response';
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

  public logIn(userType: 'patient' | 'doctor' | 'admin', email: string, password: string): Observable<AuthResponse> {
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
    return this.httpClient.post<AuthResponse>(url, {'email': email, 'password': password});
  }

  public registerPatient(
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    pin: string,
    gpId: number,
    isInsured: boolean,
  ): Observable<AuthResponse> {
    const url = `${this.api}/register-patient`;
    return this.httpClient.post<AuthResponse>(url, {
      'firstName': firstName,
      'lastName': lastName,
      'email': email,
      'password': password,
      'pin': pin,
      'gpId': gpId,
      'insured': isInsured
    });
  }

  public registerDoctor(
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    generalPractitioner: boolean,
    specialtyId: number,
  ): Observable<AuthResponse> {
    const url = `${this.api}/register-doctor`;
    return this.httpClient.post<AuthResponse>(url, {
      'firstName': firstName,
      'lastName': lastName,
      'email': email,
      'password': password,
      'generalPractitioner': generalPractitioner,
      'specialtyId': specialtyId,
    })
  }

  public logOut(): void {
    this.localStorageService.removeUserAuth();
  }
}
