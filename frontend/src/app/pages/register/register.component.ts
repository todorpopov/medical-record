import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Patient } from '../../common/patient.model';

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

    constructor(private authService: AuthService) {}

    onSubmit(form: any) {
        if(form.valid) {
            console.log("Patient created: ", this.patient)
        }
    }
}
