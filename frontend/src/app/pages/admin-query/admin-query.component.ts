import {Component} from '@angular/core';
import {RouterLink} from '@angular/router';
import {QueryType} from '../../common/util/util';
import {DropdownComponent} from '../../components/dropdown/dropdown.component';
import {ReactiveFormsModule} from '@angular/forms';
import {PatientByIdQueryComponent} from '../../query-components/patient-by-id-query/patient-by-id-query.component';
import {
  CountPatientsGpDoctorsQueryComponent
} from '../../query-components/count-patients-gp-doctors-query/count-patients-gp-doctors-query.component';
import {
  PatientsByIcdQueryComponent
} from '../../query-components/patients-by-icd-query/patients-by-icd-query.component';

@Component({
  selector: 'app-admin-query',
  imports: [
    RouterLink,
    DropdownComponent,
    ReactiveFormsModule,
    PatientByIdQueryComponent,
    CountPatientsGpDoctorsQueryComponent,
    PatientsByIcdQueryComponent
  ],
  templateUrl: './admin-query.component.html',
  styleUrl: './admin-query.component.css'
})
export class AdminQueryComponent {
  queryOptions: QueryType[] = [
    'Patients By GP Id',
    'Patients By ICD',
    'Patients By ICD Diagnosis',
    'Patients Count For All GP Doctors',
    'Doctor Visits Count',
    'Appointments For All Patients',
    'Appointments For Period - All Doctors',
    'Appointments For Period - Single Doctor',
    'Most Sick Leaves - Month',
    'Most Sick Leaves - Doctor'
  ];
  selectedQuery: QueryType | null = null;

  selectQuery(value: QueryType) {
    this.selectedQuery = value;
  }
}
