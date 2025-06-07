import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {PatientMenuDataDto} from '../common/dtos/general.dto';

@Injectable({
  providedIn: 'root'
})
export class GeneralWorkService {
  private readonly apiUrl: string = environment.apiGateway

  constructor(
    private httpClient: HttpClient
  ) { }

  public getPatientMenuData(patientId: number): Observable<HttpResponse<PatientMenuDataDto>> {
    return this.httpClient.get<PatientMenuDataDto>(
      `${this.apiUrl}/general-work/patient-menu-data?id=${patientId}`,
      {observe: 'response'}
    )
  }
}
