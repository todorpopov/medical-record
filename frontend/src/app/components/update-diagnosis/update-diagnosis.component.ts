import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {AppointmentsService} from '../../services/appointments.service';
import {IcdDto} from '../../common/dtos/icd.dto';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {TextInputComponent} from '../text-input/text-input.component';
import {DropdownComponent} from '../dropdown/dropdown.component';
import {NgIf} from '@angular/common';
import {CheckboxComponent} from '../checkbox/checkbox.component';
import {isoDateValidator} from '../../common/validators/validators';

@Component({
  selector: 'app-update-diagnosis',
  imports: [
    ReactiveFormsModule,
    TextInputComponent,
    DropdownComponent,
    NgIf,
    CheckboxComponent
  ],
  templateUrl: './update-diagnosis.component.html',
  styleUrl: './update-diagnosis.component.css'
})
export class UpdateDiagnosisComponent {
  private id: number;
  updateDiagnosis: FormGroup;

  protected includeSickLeave: boolean = false;

  protected icdEntities: IcdDto[] = [];
  protected icdFetchError: string = '';

  protected successMSg: string = '';
  protected errorMsg: string = '';

  private sickLeaveFields: string[] = ['sickLeaveDate', 'sickLeaveDays'];

  constructor(
    private fb: FormBuilder,
    private router: ActivatedRoute,
    private appointmentsService: AppointmentsService
  ) {
    this.updateDiagnosis = this.fb.group({
      treatmentDescription: [null, [Validators.required]],
      icd: [null, [Validators.required]],

      sickLeaveDate: [null],
      sickLeaveDays: [null],

      includeSickLeave: [false]
    })

    this.updateDiagnosis.get('includeSickLeave')?.valueChanges.subscribe(sickLeave => {
      this.includeSickLeave = sickLeave;
      sickLeave ? this.addSickLeaveValidators() : this.removeSickLeaveValidators()
    })

    this.fetchIcdEntities()

    const id = this.router.snapshot.paramMap.get('id');
    if (id !== null) {
      this.id = Number(id);
    } else {
      this.id = -1;
    }

    // this.appointmentsService.updateAppointment(this.id, "started", null).subscribe({
    //   error: err => {
    //     this.errorMsg = err.error.message;
    //     this.successMsg = ''
    //   }
    // })
  }

  private addSickLeaveValidators(): void {
    this.sickLeaveFields.forEach(field => {
      const control = this.updateDiagnosis.get(field);
      if (control) {
        switch (field) {
          case 'sickLeaveDate': {
            control.setValidators([Validators.required, isoDateValidator()]);
            control.updateValueAndValidity();
            break;
          }
          case 'sickLeaveDays': {
            control.setValidators([Validators.required, Validators.min(1), Validators.pattern("^[0-9]*$")]);
            control.updateValueAndValidity();
            break;
          }
        }
      }
    })
  }

  private removeSickLeaveValidators(): void {
    this.sickLeaveFields.forEach(field => {
      const control = this.updateDiagnosis.get(field);
      if (control) {
        control.clearValidators();
        control.setValue(null);
        control.updateValueAndValidity();
      }
    })
  }

  private fetchIcdEntities(): void {
    this.appointmentsService.getAllIcdEntries().then(data => {
      this.icdEntities = [...data];
    }).catch(error => {
      this.icdFetchError = error.error;
    })
  }

  onSubmit(): void {

  }
}
