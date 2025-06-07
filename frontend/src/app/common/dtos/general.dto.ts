import {PatientDto} from './patient.dto';
import {DoctorDto} from './doctor.dto';
import {AppointmentDetailedDto} from './appointment.dto';

export interface PatientMenuDataDto {
  patient: PatientDto,
  doctors: DoctorDto[],
  appointments: AppointmentDetailedDto[]
}
