import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { LoginResponse } from '../common/login.response';
import { LoginRequest } from '../common/login.request';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
  private api: string = `${environment.apiGateway}/api/auth`

  constructor(private httpClient: HttpClient) {}

  logDoctorIn(loginRequest: LoginRequest) {
    this.httpClient.post<LoginResponse>(
      `${this.api}/log-doctor-in`,
      {
        email: loginRequest.email,
        password: loginRequest.password
      }
    ).subscribe({
      next: (response) => {
        console.log(response)
        localStorage.setItem('token', response.token);
      },
      error: (error) => {
        console.log(error)
      },
      complete: () => {
        console.log("complete!")
      }
    })
  }
}
