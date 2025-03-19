import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Patient } from '../common/patient.model';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = `http://${environment.apiGatewayHost}/api/auth`

    constructor(
        private httpClient: HttpClient
    ) {}

    public registerPatient(patient: Patient) {
        this.httpClient.post(`${this.apiUrl}/register-patient`, patient)
    }
}
