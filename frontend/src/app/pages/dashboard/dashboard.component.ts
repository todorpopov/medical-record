import {Component, OnInit} from '@angular/core';
import { ColDef } from 'ag-grid-community';
import {GridComponent} from '../../components/grid/grid.component';
import {NgForOf, NgIf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {doctorColumnDefs, patientsColumnDefs, specialtyColumnDefs} from './entity.def';
import {UsersService} from '../../services/users.service';
import { PatientSummary} from '../../common/dtos/patient.dto';
import {DoctorSummary} from '../../common/dtos/doctor.dto';
import {SpecialtyDto} from '../../common/dtos/specialty.dto';
import {RowActionComponent} from '../../components/row-action/row-action.component';

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
export class DashboardComponent implements OnInit {
  entityOptions: string[] = ['Patients', 'Doctors', 'Specialties'];
  selectedEntity: 'Patients' | 'Doctors' | 'Specialties' = 'Patients';

  error: string = '';

  selectEntity(entity: string) {
    if (entity === 'Patients' || entity === 'Doctors' || entity === 'Specialties') {
      this.selectedEntity = entity;
    }
    this.error = '';
  }

  patientColumnDefs: ColDef[] = patientsColumnDefs;
  doctorColumnDefs: ColDef[] = doctorColumnDefs;
  specialtyColumnDefs: ColDef[] = specialtyColumnDefs;

  patientRowData: PatientSummary[] = [];
  doctorRowData: DoctorSummary[] = [];
  specialtyRowData: SpecialtyDto[] = [];

  constructor(
    private usersService: UsersService,
  ) {}

  ngOnInit(): void {}

  get currentColumnDefs(): ColDef[] {
    switch (this.selectedEntity) {
      case 'Patients':
        return this.patientColumnDefs;
      case 'Doctors':
        return this.doctorColumnDefs;
      case 'Specialties':
        return this.specialtyColumnDefs;
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
      default:
        return [];
    }
  }


  loadGridData() {
    switch (this.selectedEntity) {
      case 'Patients':
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
            console.log(patientSummary)
            summaryData.push(patientSummary)
          })

          this.patientRowData = [...summaryData];
        }).catch(error => {
          this.error = error.error;
        })
        break;
      case "Doctors":
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
        break;
      case "Specialties":
        this.usersService.getAllSpecialties().then(data => {
          this.specialtyRowData = [...data];
        }).catch(error => {
          this.error = error.error;
        })
    }
  }
}
