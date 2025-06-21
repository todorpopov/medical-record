import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PatientDto} from '../common/dtos/patient.dto';

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
}
