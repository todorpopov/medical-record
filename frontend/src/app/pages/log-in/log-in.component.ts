import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TextInputComponent } from "../../components/text-input/text-input.component";
import { NgIf } from '@angular/common';
import { RadioComponent } from "../../components/radio/radio.component";
import {AuthService} from '../../services/auth.service';
import {AuthResponse} from '../../common/interfaces/auth.response';
import {LocalStorageService} from '../../services/local-storage.service';

@Component({
  selector: 'app-log-in',
  imports: [
    TextInputComponent,
    ReactiveFormsModule,
    RadioComponent,
    NgIf,
    RadioComponent
  ],
  templateUrl: './log-in.component.html',
  styleUrl: './log-in.component.css',
})
export class LogInComponent implements ReactiveFormsModule {
  loginForm: FormGroup;
  label: string = 'Log In';
  logInError: string = '';

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private localStorageService: LocalStorageService,
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      userType: ['patient']
    });
  }

  private setLogInError(error: string): void {
    this.logInError = error;
  }

  private removeLogInError(): void {
    this.logInError = ''
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const userType: 'patient' | 'doctor' | 'admin' = this.loginForm.value?.userType;
      const email: string = this.loginForm.value?.email;
      const password: string = this.loginForm.value?.password;
      this.authService.logIn(userType, email, password)
        .subscribe({
          next: (auth: AuthResponse) => {
            this.localStorageService.storeUserAuth(auth);
            this.removeLogInError();
          },
          error: err => {
            console.log('Error occurred: ', err)
            this.setLogInError('An error occurred!')
          }
      });
    }
  }
}
