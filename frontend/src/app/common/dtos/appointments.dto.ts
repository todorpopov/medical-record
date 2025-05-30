import {DiagnosisDto} from './diagnosis.dto';

export interface AppointmentsDto {
  id: number,
  date: string,
  time: string,
  patientId: number,
  doctorId: number,
  status: string,
  diagnosis: DiagnosisDto | null,
}

export interface AppointmentsSummary {
  id: number,
  date: string,
  time: string,
  patientId: number,
  doctorId: number,
  status: string,
  diagnosisId: number | null,
}

export interface CreateAppointmentsDto {
  date: string,
  time: string,
  patientId: number,
  doctorId: number,
}

export interface UpdateAppointmentsDto {
  id: number,
  status: string | null,
  diagnosisId: number | null,
}

export interface AppointmentStatus {
  status: 'upcoming' | 'started' | 'finished'
}
