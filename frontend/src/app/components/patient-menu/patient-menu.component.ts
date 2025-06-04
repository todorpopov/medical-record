import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {UsersService} from '../../services/users.service';
import {LocalStorageService} from '../../services/local-storage.service';
import {DoctorName} from '../../common/dtos/doctor.dto';
import {TextInputComponent} from '../text-input/text-input.component';
import {DropdownComponent} from '../dropdown/dropdown.component';
import {NgForOf, NgIf} from '@angular/common';
import {isoDateValidator, timeValidator} from '../../common/validators/validators';
import {AppointmentsService} from '../../services/appointments.service';
import {AppointmentsDto} from '../../common/dtos/appointments.dto';

@Component({
  selector: 'app-patient-menu',
  imports: [
    ReactiveFormsModule,
    TextInputComponent,
    DropdownComponent,
    NgIf,
    NgForOf
  ],
  templateUrl: './patient-menu.component.html',
  styleUrl: './patient-menu.component.css'
})
export class PatientMenuComponent {
  createAppointmentForm: FormGroup;
  private currentPatientId: number;

  protected doctors: DoctorName[] = [];
  doctorsFetchError: string = '';

  appointmentCreateError: string = '';
  appointmentCreateSuccess: string = '';

  constructor(
    private fb: FormBuilder,
    private usersService: UsersService,
    private appointmentsService: AppointmentsService,
    private localStorageService: LocalStorageService
  ) {
    this.currentPatientId = this.localStorageService.getCurrentUserId();
    this.getAllDoctors();

    this.createAppointmentForm = this.fb.group({
      date: [null, [Validators.required, isoDateValidator()]],
      time: [null, [Validators.required, timeValidator()]],
      doctorId: [null, [Validators.required]]
    })
  }

  private getAllDoctors(): void {
    this.usersService.getAllDoctors()
      .then(data => {
        data.forEach(doctorDto => {
          const doctorName: DoctorName = {
            id: doctorDto.id,
            fullName: `${doctorDto.firstName} ${doctorDto.lastName}`
          }

          this.doctors.push(doctorName);
        })
      })
      .catch(error => {
        this.doctorsFetchError = 'Error retrieving doctors';
      })
  }

  onSubmit() {
    if (this.createAppointmentForm.valid) {
      const formValue = this.createAppointmentForm.value;

      const date = formValue.date;
      const time = formValue.time;
      const doctorId = formValue.doctorId;

      this.appointmentsService.createAppointment(
        date,
        time,
        this.currentPatientId,
        doctorId
      ).subscribe({
        next: value => {
          this.appointmentCreateSuccess = 'Successfully created new appointment'
          this.appointmentCreateError = '';
        },
        error: err => {
          this.appointmentCreateError = err.error.message;
          this.appointmentCreateSuccess = '';
        }
      })
    }
  }
}
