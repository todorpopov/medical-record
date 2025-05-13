import { Injectable } from '@angular/core';
import {UsersService} from './users.service';
import {AuthService} from './auth.service';
import {RowAction} from '../common/interfaces/row.action';

@Injectable({
  providedIn: 'root'
})
export class RowActionService {

  constructor(
    private usersService: UsersService,
    private authService: AuthService,
  ) {}

  public submitPatientDelete(id: number): RowAction {
    let successful: boolean = false;
    let message: string = ''

    this.usersService.deletePatient(id)
      .subscribe({
        next: response => {
          console.log('Response: ', response)
          successful = response.ok
        },
        error: err => {
          console.log(err.error.message)
          message = err.error.message;
        }
      })

    return {
      successful: successful,
      message: message,
    }
  }
}
