import {SpecialtyDto} from './specialty.dto';

export interface DoctorDto {
  id: number,
  firstName: string,
  lastName: string,
  email: string,
  password: string,
  specialty: SpecialtyDto,
  gp: boolean
}

export interface DoctorSummary {
  id: number,
  firstName: string,
  lastName: string,
  email: string,
  password: string,
  specialtyId: number
  gp: boolean,
}
