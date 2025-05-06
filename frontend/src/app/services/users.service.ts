import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

import {Specialty} from '../common/interfaces/specialty';
import {DoctorSummary} from '../common/interfaces/doctor.summary';
import {firstValueFrom} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private apiUrl = `${environment.apiGateway}/users`

  constructor(
    private httpClient: HttpClient
  ) {}

  public async getSpecialties(): Promise<Specialty[]> {
    try {
      return await firstValueFrom(this.httpClient.get<Specialty[]>(`${this.apiUrl}/specialty/all`));
    } catch (error) {
      console.log('Error fetching specialties: ', error)
      return [];
    }
  }

  public async getGpDoctors(): Promise<DoctorSummary[]> {
    try {
      return await firstValueFrom(this.httpClient.get<DoctorSummary[]>(`${this.apiUrl}/doctors/all-gp`));
    } catch (error) {
      console.log('Error fetching doctors: ', error);
      return [];
    }
  }
}
