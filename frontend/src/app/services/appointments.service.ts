import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {AppointmentsDto} from '../common/dtos/appointments.dto';
import {firstValueFrom, Observable} from 'rxjs';
import {ApiResponse} from '../common/interfaces/api.response';
import {DiagnosisDto} from '../common/dtos/diagnosis.dto';
import {IcdDto} from '../common/dtos/icd.dto';
import {SickLeaveDto} from '../common/dtos/sick-leave.dto';

@Injectable({
  providedIn: 'root'
})
export class AppointmentsService {
  private readonly apiUrl: string = environment.apiGateway

  constructor(
    private httpClient: HttpClient,
  ) { }

  public async getAllAppointments(): Promise<AppointmentsDto[]> {
    return firstValueFrom(this.httpClient.get<AppointmentsDto[]>(`${this.apiUrl}/appointments/list/all`));
  }

  public createAppointment(date: string, time: string, patientId: number, doctorId: number): Observable<HttpResponse<ApiResponse>> {
    let body: any = {
      date: date,
      time: time,
      patientId: patientId,
      doctorId: doctorId
    };

    return this.httpClient.post<ApiResponse>(`${this.apiUrl}/appointments/create`, body, {observe: 'response'});
  }

  public updateAppointment(id: number, status: string | null, diagnosisId: number | null): Observable<HttpResponse<ApiResponse>> {
    let body: any = {};
    body.id = id;

    if (status) {
      body.status = status;
    }

    if (diagnosisId) {
      body.diagnosisId = diagnosisId;
    }

    return this.httpClient.put<ApiResponse>(`${this.apiUrl}/appointments/update`, body, {observe: 'response'});
  }

  public deleteAppointment(id: number): Observable<HttpResponse<ApiResponse>> {
    return this.httpClient.delete<ApiResponse>(`${this.apiUrl}/appointments/delete?id=${id}`, {observe: 'response'});
  }

  public async getAllDiagnoses(): Promise<DiagnosisDto[]> {
    return firstValueFrom(this.httpClient.get<DiagnosisDto[]>(`${this.apiUrl}/diagnosis/list/all`));
  }

  public async getAllIcdEntries(): Promise<IcdDto[]> {
    return firstValueFrom(this.httpClient.get<IcdDto[]>(`${this.apiUrl}/icd/list/all`));
  }

  public async getAllSickLeaveEntities(): Promise<SickLeaveDto[]> {
    return firstValueFrom(this.httpClient.get<SickLeaveDto[]>(`${this.apiUrl}/sick-leave/list/all`));
  }
}
