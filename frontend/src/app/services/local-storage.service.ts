import { Injectable } from '@angular/core';
import {UserAuth} from '../common/interfaces/user.auth';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  constructor() { }

  public storeUserAuth(userAuth: UserAuth): void {
    localStorage.setItem('token', userAuth.token);
    localStorage.setItem('role', userAuth.role);
  }

  public getUserAuth(): UserAuth {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role')

    return {
      'token': token !== null ? token : '',
      'role': role !== null ? role : '',
    }
  }

  public getUserToken(): string | null {
    return localStorage.getItem('token')
  }

  public getUserRole(): string | null {
    return localStorage.getItem('role')
  }

  public removeUserAuth(): void {
    localStorage.removeItem('auth');
  }
}
