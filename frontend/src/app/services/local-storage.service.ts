import {Injectable} from '@angular/core';
import {AuthResponse} from '../common/interfaces/auth.response';
import {first} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {
  private currentUser: string | null = null;

  constructor() {}

  public setUserAuth(userAuth: AuthResponse): void {
    localStorage.setItem('token', userAuth.token);
    localStorage.setItem('id', userAuth.id.toString())
    localStorage.setItem('email', userAuth.email);
    localStorage.setItem('firstName', userAuth.firstName);
    localStorage.setItem('lastName', userAuth.lastName);
    localStorage.setItem('role', userAuth.role);

    this.currentUser = `${userAuth.firstName} ${userAuth.lastName}`;
  }

  public getUserAuth(): AuthResponse {
    const token = localStorage.getItem('token');
    const id = localStorage.getItem('id');
    const email = localStorage.getItem('email');
    const firstName = localStorage.getItem('firstName');
    const lastName = localStorage.getItem('lastName');
    const role = localStorage.getItem('role');

    return {
      'token': token !== null ? token : '',
      'role': role !== null ? role : '',
      'id': id !== null ? +id : -1,
      'email': email !== null ? email : '',
      'firstName': firstName !== null ? firstName : '',
      'lastName': lastName !== null ? lastName : '',
    }
  }

  public isUserLoggedIn(): boolean {
    return localStorage.getItem('token') !== null;
  }

  public getUserToken(): string | null {
    return localStorage.getItem('token')
  }

  public getUserRole(): string | null {
    return localStorage.getItem('role')
  }

  public getUserFirstName(): string | null {
    return localStorage.getItem('firstName')
  }

  public getCurrentUserId(): number {
    return Number(localStorage.getItem('id'));
  }

  public removeUserAuth(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('id');
    localStorage.removeItem('email');
    localStorage.removeItem('firstName');
    localStorage.removeItem('lastName');

    this.currentUser = null;
  }

  public getCurrentUser(): string | null {
    return this.currentUser;
  }
}
