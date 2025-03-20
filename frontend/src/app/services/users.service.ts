import { Injectable } from '@angular/core';
import {Patient} from '../common/patient.model';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsersService {
    private apiUrl = `http://${environment.apiGatewayHost}:${environment.apiGatewayPort}/api/users`

    constructor(
        private httpClient: HttpClient
    ) { }

    public registerPatient(patient: Patient) {
        this.httpClient.post(`${this.apiUrl}/create`, patient)
            .subscribe({
                next: () => {
                    console.log("Successfully created patient")
                },
                error: (err: Error) => {
                    console.log(`Error occurred: ${err.message}`)
                }
            })
    }
}
