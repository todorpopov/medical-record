export interface AppointmentsDto {
  id: number,
  date: string,
  time: string,
  patientId: number,
  doctorId: number,
  status: string,
}

export interface CreateAppointmentsDto {
  date: string,
  time: string,
  patientId: number,
  doctorId: number,
}

export interface UpdateAppointmentsDto {
  id: number,
  status: string,
}

export interface AppointmentStatus {
  status: 'upcoming' | 'started' | 'finished'
}
