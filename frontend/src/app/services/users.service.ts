import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {environment} from '../../environments/environment';

import {DoctorDto, DoctorSummary} from '../common/interfaces/doctor.dto';
import {firstValueFrom, Observable} from 'rxjs';
import {PatientDto} from '../common/interfaces/patient.dto';
import {SpecialtyDto} from '../common/interfaces/specialty.dto';
import {ApiResponse} from '../common/interfaces/api.response';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private apiUrl = `${environment.apiGateway}/users`

  constructor(
    private httpClient: HttpClient
  ) {}

  public async getGpDoctors(): Promise<DoctorSummary[]> {
    return await firstValueFrom(this.httpClient.get<DoctorSummary[]>(`${this.apiUrl}/doctors/list/gp`));
  }

  public async getAllPatients(): Promise<PatientDto[]> {
    return await firstValueFrom(this.httpClient.get<PatientDto[]>(`${this.apiUrl}/patients/list/all`));
  }

  public async getAllDoctors(): Promise<DoctorDto[]> {
    return await firstValueFrom(this.httpClient.get<DoctorDto[]>(`${this.apiUrl}/doctors/list/all`));
  }

  public async getAllSpecialties(): Promise<SpecialtyDto[]> {
    return await firstValueFrom(this.httpClient.get<SpecialtyDto[]>(`${this.apiUrl}/specialty/list/all`));
  }

  public deletePatient(id: number): Observable<HttpResponse<ApiResponse>> {
    return this.httpClient.delete<ApiResponse>(`${this.apiUrl}/patients/delete?id=${id}`, {observe: 'response'});
  }
}
