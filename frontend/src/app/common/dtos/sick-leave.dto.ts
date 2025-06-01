export interface SickLeaveDto {
  id: number,
  date: string,
  numberOfDays: number,
}

export interface CreateSickLeaveDto {
  date: string,
  numberOfDays: number,
}

export interface UpdateSickLeaveDto {
  id: number,
  date: string,
  numberOfDays: number,
}
