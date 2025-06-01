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

  public createDiagnosis(treatmentDescription: string, icdId: number, sickLeaveDate: number | null, sickLeaveDays: number | null): Observable<HttpResponse<ApiResponse>> {
    let body: any = {};
    body.treatmentDescription = treatmentDescription;
    body.icdId = icdId;

    if (sickLeaveDate && sickLeaveDays) {
      body.sickLeaveDate = sickLeaveDate;
      body.sickLeaveDays = sickLeaveDays;
    }

    return this.httpClient.post<ApiResponse>(`${this.apiUrl}/diagnosis/create`, body, {observe: 'response'});
  }

  public updateDiagnosis(id: number, treatmentDescription: string, icdId: number, sickLeaveDate: number | null, sickLeaveDays: number | null): Observable<HttpResponse<ApiResponse>> {
    let body: any = {}
    body.id = id;

    if (treatmentDescription) {
      body.treatmentDescription = treatmentDescription;
    }

    if (icdId) {
      body.icdId = icdId;
    }

    if (sickLeaveDate && sickLeaveDays) {
      body.sickLeaveDate = sickLeaveDate;
      body.sickLeaveDays = sickLeaveDays;
    }

    return this.httpClient.put<ApiResponse>(`${this.apiUrl}/diagnosis/update`, body, {observe: 'response'});
  }

  public deleteDiagnosis(id: number): Observable<HttpResponse<ApiResponse>> {
    return this.httpClient.delete<ApiResponse>(`${this.apiUrl}/diagnosis/delete?id=${id}`, {observe: 'response'});
  }

  public async getAllIcdEntries(): Promise<IcdDto[]> {
    return firstValueFrom(this.httpClient.get<IcdDto[]>(`${this.apiUrl}/icd/list/all`));
  }

  public createIcd(icdCode: string, icdDescription: string): Observable<HttpResponse<ApiResponse>> {
    const body: any = {
      code: icdCode,
      description: icdDescription
    }

    return this.httpClient.post<ApiResponse>(`${this.apiUrl}/icd/create`, body, {observe: 'response'});
  }

  public updateIcd(id: number, icdCode: string | null, icdDescription: string | null): Observable<HttpResponse<ApiResponse>> {
    let body: any = {}
    body.id = id;

    if (icdCode) {
      body.code = icdCode;
    }

    if (icdDescription) {
      body.description = icdDescription;
    }

    return this.httpClient.put<ApiResponse>(`${this.apiUrl}/icd/update`, body, {observe: 'response'});
  }

  public deleteIcd(id: number): Observable<HttpResponse<ApiResponse>> {
    return this.httpClient.delete<ApiResponse>(`${this.apiUrl}/icd/delete?id=${id}`, {observe: 'response'});
  }

  public async getAllSickLeaveEntities(): Promise<SickLeaveDto[]> {
    return firstValueFrom(this.httpClient.get<SickLeaveDto[]>(`${this.apiUrl}/sick-leave/list/all`));
  }

  public createSickLeave(date: string, days: number): Observable<HttpResponse<ApiResponse>> {
    const body = {
      date: date,
      numberOfDays: days
    }

    return this.httpClient.post<ApiResponse>(`${this.apiUrl}/sick-leave/create`, body, {observe: 'response'});
  }

  public updateSickLeave(id: number, date: string | null, days: number | null): Observable<HttpResponse<ApiResponse>> {
    let body: any = {}
    body.id = id;

    if (date) {
      body.date = date;
    }

    if (days) {
      body.numberOfDays = days;
    }

    return this.httpClient.put<ApiResponse>(`${this.apiUrl}/sick-leave/update`, body, {observe: 'response'});
  }

  public deleteSickLeave(id: number): Observable<HttpResponse<ApiResponse>> {
    return this.httpClient.delete<ApiResponse>(`${this.apiUrl}/sick-leave/delete?id=${id}`, {observe: 'response'});
  }
}
