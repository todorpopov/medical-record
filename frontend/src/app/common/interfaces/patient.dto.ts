import {DoctorDto} from './doctor.dto';

export interface PatientDto {
  id: number,
  firstName: string,
  lastName: string,
  email: string,
  password: string,
  pin: string,
  gp: DoctorDto,
  insured: boolean
}

export interface PatientSummary {
  id: number,
  firstName: string,
  lastName: string,
  email: string,
  password: string,
  pin: string,
  gpId: number,
  insured: boolean
}
