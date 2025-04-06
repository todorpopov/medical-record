import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TextInputComponent } from "../../components/text-input/text-input.component";
import { NgIf } from '@angular/common';
import { RadioComponent } from "../../components/radio/radio.component";

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
export class LogInComponent {
  loginForm: FormGroup;
  label: string = 'Log In';

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      userType: ['patient']
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const userType = this.loginForm.value.userType;
      console.log('Logging in as:', userType);
    }
  }
}
