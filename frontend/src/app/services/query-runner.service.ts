import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PatientDto} from '../common/dtos/patient.dto';
import {PatientCount} from '../common/dtos/queries.dto';
import {IcdOccurrenceDto} from '../common/dtos/icd.dto';
import {DoctorAppointmentsCount} from '../common/dtos/appointment.dto';

@Injectable({
  providedIn: 'root'
})
export class QueryRunnerService {
  private readonly apiUrl: string = `${environment.apiGateway}/query`

  constructor(
    private httpClient: HttpClient,
  ) {}

  public getAllPatientsByGpId(gpId: number): Observable<HttpResponse<PatientDto[]>> {
    return this.httpClient.get<PatientDto[]>(`${this.apiUrl}/get-all-patients-for-gp/${gpId}`, {observe: 'response'})
  }

  public countOfPatientsForDoctors(): Observable<HttpResponse<PatientCount[]>> {
    return this.httpClient.get<PatientCount[]>(`${this.apiUrl}/get-patients-count-for-gp-doctors`, {observe: 'response'})
  }

  public getPatientsByIcd(icdId: number): Observable<HttpResponse<PatientDto[]>> {
    return this.httpClient.get<PatientDto[]>(`${this.apiUrl}/get-patients-by-icd/${icdId}`, {observe: 'response'})
  }

  public getIcdOccurrences(limit: number): Observable<HttpResponse<IcdOccurrenceDto[]>> {
    return this.httpClient.get<IcdOccurrenceDto[]>(`${this.apiUrl}/get-most-frequent-icds/${limit}`, {observe: 'response'})
  }

  public getDoctorAppointmentsCount(): Observable<HttpResponse<DoctorAppointmentsCount[]>> {
    return this.httpClient.get<DoctorAppointmentsCount[]>(`${this.apiUrl}/get-doctor-appointments-count`, {observe: 'response'})
  }
}
