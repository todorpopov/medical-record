import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {UsersService} from '../../services/users.service';
import {LocalStorageService} from '../../services/local-storage.service';
import {DoctorDto} from '../../common/dtos/doctor.dto';
import {TextInputComponent} from '../text-input/text-input.component';
import {DropdownComponent} from '../dropdown/dropdown.component';
import {NgForOf, NgIf} from '@angular/common';
import {isoDateValidator, timeValidator} from '../../common/validators/validators';
import {AppointmentsService} from '../../services/appointments.service';
import {AppointmentAllDetailsDto} from '../../common/dtos/appointment.dto';

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

  protected doctors: DoctorDto[] = [];
  doctorsFetchError: string = '';

  appointmentCreateError: string = '';
  appointmentCreateSuccess: string = '';

  protected appointments: AppointmentAllDetailsDto[] = [];
  appointmentsFetchError: string = '';

  constructor(
    private fb: FormBuilder,
    private usersService: UsersService,
    private appointmentsService: AppointmentsService,
    private localStorageService: LocalStorageService
  ) {
    this.currentPatientId = this.localStorageService.getCurrentUserId();
    this.getData();

    this.createAppointmentForm = this.fb.group({
      date: [null, [Validators.required, isoDateValidator()]],
      time: [null, [Validators.required, timeValidator()]],
      doctorId: [null, [Validators.required]]
    })
  }

  private getData(): void {
    this.usersService.getAllDoctors()
      .then(data => {
        data.forEach(doctorDto => {
          this.doctors.push(doctorDto);
        })
      })
      .catch(error => {
        this.doctorsFetchError = 'Error retrieving doctors';
      })

    this.appointmentsService.getAllAppointments()
      .then(data => {
        data.forEach(appointment => {
          const dto: AppointmentAllDetailsDto = {
            id: appointment.id,
            date: appointment.date,
            time: appointment.time,
            patient: null,
            doctor: this.findDoctor(appointment.doctorId),
            status: appointment.status,
            diagnosis: appointment.diagnosis
          }

          this.appointments.push(dto);
        })
      })
      .catch(error => {
        this.appointmentsFetchError = 'Error fetching doctors';
      })
  }

  private findDoctor(id: number): DoctorDto | null {
    for (const doctor of this.doctors) {
      if (doctor.id === id) {
        return doctor;
      }
    }
    return null;
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
