import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Patient } from '../../common/patient.model';
import {UsersService} from '../../services/users.service';

@Component({
    standalone: true,
    selector: 'app-register',
    imports: [FormsModule],
    templateUrl: './register.component.html',
    styleUrl: './register.component.css'
})
export class RegisterComponent {
    patient: Patient = {
        email: '',
        firstName: '',
        lastName: '',
        pin: '',
        password: ''
    }

    constructor(private usersService: UsersService) {}

    onSubmit(form: any) {
        if(form.valid) {
            this.usersService.registerPatient(this.patient)
        }
    }
}
