import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { UsersService } from '../../services/users.service';
import { NgIf } from '@angular/common';
import { RadioComponent } from "../../components/radio/radio.component";
import { TextInputComponent } from '../../components/text-input/text-input.component';
import { CheckboxComponent } from "../../components/checkbox/checkbox.component";
import { DropdownComponent } from '../../components/dropdown/dropdown.component';
import { DoctorSummary } from '../../common/interfaces/doctor.summary';
import { Specialty } from '../../common/interfaces/specialty';
import {AuthService} from '../../services/auth.service';
import {AuthResponse} from '../../common/interfaces/auth.response';
import {LocalStorageService} from '../../services/local-storage.service';

@Component({
  selector: 'app-register',
  imports: [
    ReactiveFormsModule,
    NgIf,
    RadioComponent,
    TextInputComponent,
    CheckboxComponent,
    DropdownComponent,
],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements ReactiveFormsModule{
  registerForm: FormGroup;
  label: string = 'Register';
  selectedUserType: 'patient' | 'doctor' = 'patient';

  specialties: Specialty[] = []
  selectedSpecialty: Specialty | null = null;
  specialtyError: string = ''

  gpDoctors: DoctorSummary[] = []
  selectedDoctor: DoctorSummary | null = null;
  doctorError: string = '';


  constructor(
    private fb: FormBuilder,
    private usersService: UsersService,
    private authService: AuthService,
    private localStorageService: LocalStorageService,
  ) {
    this.getGpDoctors();
    this.getSpecialties();

    this.registerForm = this.fb.group({
      userType: ['patient'],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],

      // Patient specific
      gpId: [null],
      pin: ['', [Validators.required, Validators.pattern('[0-9]{10}')]],
      isHealthInsured: [true],

      // Doctor specific
      specialtyId: [null],
      isGp: [true]
    })

    this.registerForm.get('userType')?.valueChanges.subscribe(userType => {
      this.selectedUserType = userType;
      this.updateFormValidation();
    })

    // Initialize validation based on initial user type
    this.updateFormValidation();
  }

  private async getGpDoctors() {
    const apiData = await this.usersService.getGpDoctors();
    this.gpDoctors = apiData;

    if (apiData.length === 0) {
      this.doctorError = 'Error retrieving doctors';
    } else {
      this.doctorError = '';
    }
  }

  private async getSpecialties() {
    const apiData = await this.usersService.getSpecialties();
    this.specialties = apiData;

    if (apiData.length === 0) {
      this.specialtyError = 'Error retrieving specialties';
    } else  {
      this.specialtyError = '';
    }
  }

  onDoctorSelected(doctor: DoctorSummary) {
    this.selectedDoctor = doctor;
  }

  onSpecialtySelected(specialty: Specialty) {
    this.selectedSpecialty = specialty;
  }

  private updateFormValidation(): void {
    const patientFields = ['gpId', 'pin', 'isHealthInsured'];
    const doctorFields = ['specialtyId', 'isGp'];

    if (this.selectedUserType === 'patient') {
      // Set required validation for patient fields
      patientFields.forEach(field => {
        const control = this.registerForm.get(field);
        if (control) {
          control.setValidators([Validators.required]);
          control.updateValueAndValidity();
        }
      });

      // Clear validation for doctor fields
      doctorFields.forEach(field => {
        const control = this.registerForm.get(field);
        if (control) {
          control.clearValidators();
          control.updateValueAndValidity();
          control.setValue(null); // Reset the value
        }
      });
    } else {
      // Set required validation for doctor fields
      doctorFields.forEach(field => {
        const control = this.registerForm.get(field);
        if (control) {
          control.setValidators([Validators.required]);
          control.updateValueAndValidity();
        }
      });

      // Clear validation for patient fields
      patientFields.forEach(field => {
        const control = this.registerForm.get(field);
        if (control) {
          control.clearValidators();
          control.updateValueAndValidity();
          control.setValue(null); // Reset the value
        }
      });
    }
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const formValue = this.registerForm.value;

      switch (formValue.userType) {
        case 'patient': {
          this.authService.registerPatient(
            formValue.firstName,
            formValue.lastName,
            formValue.email,
            formValue.password,
            formValue.pin,
            formValue.gpId,
            formValue.isHealthInsured
          ).subscribe({
            next: (authResponse: AuthResponse) => {
              if (authResponse.successful) {
                this.localStorageService.storeUserAuth(authResponse);
              }
            }
          });
          break;
        }
        case 'doctor': {
          this.authService.registerDoctor(
            formValue.firstName,
            formValue.lastName,
            formValue.email,
            formValue.password,
            formValue.isGp,
            formValue.specialtyId
          ).subscribe({
            next: (authResponse: AuthResponse) => {
              if (authResponse.successful) {
                this.localStorageService.storeUserAuth(authResponse);
              }
            }
          })
          break;
        }
      }
    }
  }
}

