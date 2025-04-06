import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { LoginResponse } from '../common/login.response';
import { LoginRequest } from '../common/login.request';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
  private api: string = `${environment.apiGateway}/api/auth`

  constructor(private httpClient: HttpClient) {}

  protected logDoctorIn(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.httpClient.post<LoginResponse>(
      `${this.api}/log-doctor-in`,
      {
        email: loginRequest.email,
        password: loginRequest.password
      }
    )
  }
}
