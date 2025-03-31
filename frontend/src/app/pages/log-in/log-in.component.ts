import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TextInputComponent } from "../../components/text-input/text-input.component";
import { CheckboxComponent } from "../../components/checkbox/checkbox.component";

@Component({
  selector: 'app-log-in',
  imports: [
    TextInputComponent,
    ReactiveFormsModule,
    CheckboxComponent
],
  templateUrl: './log-in.component.html',
  styleUrl: './log-in.component.css',
})
export class LogInComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      isDoctor: [false]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      if (this.loginForm.get('isDoctor') === true) {
        
      }
    }
  }
}
