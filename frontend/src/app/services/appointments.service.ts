import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {AppointmentsDto} from '../common/dtos/appointments.dto';
import {firstValueFrom, Observable} from 'rxjs';
import {ApiResponse} from '../common/interfaces/api.response';

@Injectable({
  providedIn: 'root'
})
export class AppointmentsService {
  private readonly apiUrl: string = `${environment.apiGateway}/appointments`

  constructor(
    private httpClient: HttpClient,
  ) { }

  public async getAllAppointments(): Promise<AppointmentsDto[]> {
    return firstValueFrom(this.httpClient.get<AppointmentsDto[]>(`${this.apiUrl}/list/all`));
  }

  public createAppointment(date: string, time: string, patientId: number, doctorId: number): Observable<HttpResponse<ApiResponse>> {
    let body: any = {
      date: date,
      time: time,
      patientId: patientId,
      doctorId: doctorId
    };

    return this.httpClient.post<ApiResponse>(`${this.apiUrl}/create`, body, {observe: 'response'});
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

    return this.httpClient.put<ApiResponse>(`${this.apiUrl}/update`, body, {observe: 'response'});
  }

  public deleteAppointment(id: number): Observable<HttpResponse<ApiResponse>> {
    return this.httpClient.delete<ApiResponse>(`${this.apiUrl}/delete?id=${id}`, {observe: 'response'});
  }
}
