import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsersService } from '../../services/users.service';
import { NgIf } from '@angular/common';
import { RadioComponent } from "../../components/radio/radio.component";
import { TextInputComponent } from '../../components/text-input/text-input.component';
import { CheckboxComponent } from "../../components/checkbox/checkbox.component";
import { DropdownComponent } from '../../components/dropdown/dropdown.component';
import { DoctorSummaryDTO } from '../../common/dtos/doctor.summary.dto';
import { SpecialtyDTO } from '../../common/dtos/specialty.dto';

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
export class RegisterComponent {
  registerForm: FormGroup;
  label: string = 'Register';
  selectedUserType: 'patient' | 'doctor' = 'patient';

  specialties: SpecialtyDTO[] = []
  selectedSpecialtyId: number | null = null;
  selectedSpecialty: SpecialtyDTO | null = null;
  specialtyError: string = ''

  gpDoctors: DoctorSummaryDTO[] = []
  selectedDoctorId: number | null = null;
  selectedDoctor: DoctorSummaryDTO | null = null;
  doctorError: string = '';


  constructor(
    private fb: FormBuilder,
    private usersService: UsersService
  ) {
    this.getGpDoctors();
    this.getSpecialties();
  
    this.registerForm = this.fb.group({
      userType: ['patient'],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      firstName: [''],
      lastName: [''],
      
      // Patient specific
      gpId: [-1],
      pin: ['', [Validators.required, Validators.pattern('[0-9]{10}')]],
      isHealthInsured: [true],

      // Doctor specific
      specialtyId: [-1],
      isGp: [true]
    })

    this.registerForm.get('userType')?.valueChanges.subscribe(userType => {
      this.selectedUserType = userType;
      this.updateFormVlaidation();
    })
  }

  private getGpDoctors() {
    this.gpDoctors = this.usersService.getGpDoctors();
  }

  private getSpecialties() {
    this.specialties = this.usersService.getSpecialties();
  }

  onDoctorSelected(doctor: DoctorSummaryDTO) {
    this.selectedDoctor = doctor;
  }

  onSpecialtySelected(specialty: SpecialtyDTO) {
    this.selectedSpecialty = specialty;
  }

  private updateFormVlaidation(): void {
    const patientFields = ['gpId', 'pin', 'isHealthInsured'];
    const doctorFields = ['specialtyId', 'isGp'];

    if (this.selectedUserType === 'patient') {
      patientFields.forEach(field => {
        this.registerForm.get(field)?.setValidators([Validators.required]);
        this.registerForm.get(field)?.updateValueAndValidity();
      })

      doctorFields.forEach(field => {
        this.registerForm.get(field)?.clearValidators();
        this.registerForm.get(field)?.updateValueAndValidity();
      })
    } else {
      doctorFields.forEach(field => {
        this.registerForm.get(field)?.setValidators([Validators.required]);
        this.registerForm.get(field)?.updateValueAndValidity();
      })

      patientFields.forEach(field => {
        this.registerForm.get(field)?.clearValidators();
        this.registerForm.get(field)?.updateValueAndValidity();
      })
    }
  }

  onSubmit(): void {
    console.log(this.registerForm.value)
  }
}

