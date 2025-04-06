import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

import { SpecialtyDTO } from '../common/dtos/specialty.dto';
import { DoctorSummaryDTO } from '../common/dtos/doctor.summary.dto';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
  private apiUrl = `${environment.apiGateway}/users`

  constructor(
    private httpClient: HttpClient
  ) {}

  public getSpecialties(): SpecialtyDTO[] {
    let dtos: SpecialtyDTO[] = [];
    this.httpClient.get<SpecialtyDTO[]>(`${this.apiUrl}/specialty/all`).subscribe({
      next: (specialties: SpecialtyDTO[]) => {
        specialties.forEach(specialty => {
          dtos.push(specialty)
        })
      },
      error: (err) => {
        console.log("Error fetching specialties", err);
      }
    });

    return dtos;
  }

  public getGpDoctors(): DoctorSummaryDTO[] {
    let dtos: DoctorSummaryDTO[] = [];
    this.httpClient.get<DoctorSummaryDTO[]>(`${this.apiUrl}/doctors/all-gp`).subscribe({
      next: ((doctors: DoctorSummaryDTO[]) => {
        doctors.forEach(doctor => {
          dtos.push(doctor)
        })
      }),
      error: (err) => {
        console.log("Error fetching GP doctors")
      }
    })

    return dtos;
  }
}
