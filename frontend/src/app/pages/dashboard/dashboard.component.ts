import {Component} from '@angular/core';
import { ColDef } from 'ag-grid-community';
import {GridComponent} from '../../components/grid/grid.component';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {appointmentsColumnDefs, doctorColumnDefs, patientsColumnDefs, specialtyColumnDefs} from './entity.def';
import {UsersService} from '../../services/users.service';
import { PatientSummary} from '../../common/dtos/patient.dto';
import {DoctorSummary} from '../../common/dtos/doctor.dto';
import {SpecialtyDto} from '../../common/dtos/specialty.dto';
import {RowActionComponent} from '../../components/row-action/row-action.component';
import {AppointmentsService} from '../../services/appointments.service';
import {AppointmentsDto} from '../../common/dtos/appointments.dto';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    GridComponent,
    RowActionComponent,
    NgForOf,
    FormsModule,
    NgIf,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  entityOptions: string[] = ['Patients', 'Doctors', 'Specialties', 'Appointments'];
  selectedEntity: 'Patients' | 'Doctors' | 'Specialties' | 'Appointments' = 'Patients';

  error: string = '';

  selectEntity(entity: string) {
    if (entity === 'Patients' || entity === 'Doctors' || entity === 'Specialties' || entity === 'Appointments') {
      this.selectedEntity = entity;
    }
    this.error = '';
  }

  patientColumnDefs: ColDef[] = patientsColumnDefs;
  doctorColumnDefs: ColDef[] = doctorColumnDefs;
  specialtyColumnDefs: ColDef[] = specialtyColumnDefs;
  appointmentsColumnDefs: ColDef[] = appointmentsColumnDefs;

  patientRowData: PatientSummary[] = [];
  doctorRowData: DoctorSummary[] = [];
  specialtyRowData: SpecialtyDto[] = [];
  appointmentsRowData: AppointmentsDto[] = [];

  constructor(
    private usersService: UsersService,
    private appointmentsService: AppointmentsService,
  ) {}

  get currentColumnDefs(): ColDef[] {
    switch (this.selectedEntity) {
      case 'Patients':
        return this.patientColumnDefs;
      case 'Doctors':
        return this.doctorColumnDefs;
      case 'Specialties':
        return this.specialtyColumnDefs;
      case 'Appointments':
        return this.appointmentsColumnDefs;
      default:
        return [];
    }
  }

  get currentRowData(): any[] {
    switch (this.selectedEntity) {
      case 'Patients':
        return this.patientRowData;
      case 'Doctors':
        return this.doctorRowData;
      case 'Specialties':
        return this.specialtyRowData;
      case 'Appointments':
        return this.appointmentsRowData;
      default:
        return [];
    }
  }

  loadGridData() {
    switch (this.selectedEntity) {
      case 'Patients':
        this.handlePatientsFetch();
        break;
      case "Doctors":
        this.handleDoctorsFetch();
        break;
      case "Specialties":
        this.handleSpecialtiesFetch();
        break;
      case "Appointments":
        this.handleAppointmentsFetch();
        break;
    }
  }

  private handlePatientsFetch(): void {
    this.usersService.getAllPatients().then(data => {
      const summaryData: PatientSummary[] = []
      data.forEach(patient => {
        const patientSummary: PatientSummary = {
          id: patient.id,
          firstName: patient.firstName,
          lastName: patient.lastName,
          email: patient.email,
          password: patient.password,
          pin: patient.pin,
          gpId: patient.gp.id,
          insured: patient.insured,
        }
        summaryData.push(patientSummary)
      })

      this.patientRowData = [...summaryData];
    }).catch(error => {
      this.error = error.error;
    })
  }

  private handleDoctorsFetch(): void {
    this.usersService.getAllDoctors().then(data => {
      const summaryData: DoctorSummary[] = []
      data.forEach(doctor => {
        const doctorSummary: DoctorSummary = {
          id: doctor.id,
          firstName: doctor.firstName,
          lastName: doctor.lastName,
          email: doctor.email,
          password: doctor.password,
          specialtyId: doctor.specialty.id,
          gp: doctor.gp,
        }
        summaryData.push(doctorSummary)
      })

      this.doctorRowData = [...summaryData];
    }).catch(error => {
      this.error = error.error;
    })
  }

  private handleSpecialtiesFetch(): void {
    this.usersService.getAllSpecialties().then(data => {
      this.specialtyRowData = [...data];
    }).catch(error => {
      this.error = error.error;
    })
  }

  private handleAppointmentsFetch(): void {
    this.appointmentsService.getAllAppointments().then(data => {
      this.appointmentsRowData = [...data];
    }).catch(error => {
      this.error = error.error;
    })
  }
}
