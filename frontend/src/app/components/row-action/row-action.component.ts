import {Component, Input, OnChanges} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {DropdownComponent} from '../dropdown/dropdown.component';
import {NgIf} from '@angular/common';
import {TextInputComponent} from '../text-input/text-input.component';
import {CheckboxComponent} from '../checkbox/checkbox.component';
import {UsersService} from '../../services/users.service';

interface Action {
  name: 'Create' | 'Update' | 'Delete';
}

@Component({
  selector: 'app-row-action',
  imports: [
    ReactiveFormsModule,
    DropdownComponent,
    NgIf,
    TextInputComponent,
    CheckboxComponent
  ],
  templateUrl: './row-action.component.html',
  styleUrl: './row-action.component.css'
})
export class RowActionComponent implements ReactiveFormsModule, OnChanges {
  rowActionForm: FormGroup;

  @Input() selectedEntity: string = '';

  @Input() entitiesList: any[] = [];


  rowActionError: string = '';
  rowActionSuccess: string = '';

  actions: Action[] = [
    { name: 'Create' },
    { name: 'Update' },
    { name: 'Delete' }
  ];
  selectedAction: Action =  { name: 'Create' };

  private readonly createPatientFields: string[] = ['firstName', 'lastName', 'email', 'password', 'pin', 'gpId'];
  private readonly createDoctorFields: string[] = ['firstName', 'lastName', 'email', 'password', 'specialtyId'];
  private readonly createSpecialtyFields: string[] = ['specialtyName', 'specialtyDescription'];

  private readonly entityIdField: string[] = ['entityId'];

  private readonly allFields: string[] = [
    'entityId',
    'email',
    'password',
    'firstName',
    'lastName',
    'gpId',
    'pin',
    'specialtyId',
    'specialtyName',
    'specialtyDescription'
  ]

  constructor(
    private formBuilder: FormBuilder,
    private usersService: UsersService,
  ) {
    this.rowActionForm = this.formBuilder.group({
      action: [null, [Validators.required]],

      entityId: [null],

      email: [null],
      password: [null],
      firstName: [null],
      lastName: [null],

      gpId: [null],
      pin: [null],
      isHealthInsured: [true],

      specialtyId: [null],
      isGp: [null],

      specialtyName: [null],
      specialtyDescription: [null],
    });

    this.rowActionForm.get('action')?.valueChanges.subscribe(action => {
      this.updateFormValidation();
    })

    this.updateFormValidation();
  }

  clearLabels() {
    this.rowActionError = '';
    this.rowActionSuccess = '';
  }

  ngOnChanges(): void {
    this.clearLabels();
  }

  updateFormValidation() {
    const action = this.rowActionForm.get('action')?.value;
    const entity = this.selectedEntity;

    this.resetAllFields();
    if (action === 'Create') {
      switch (entity) {
        case 'Patients': {
          this.setFormValidation(this.createPatientFields);
          break;
        }
        case 'Doctors': {
          this.setFormValidation(this.createDoctorFields);
          break;
        }
        case 'Specialties': {
          this.setFormValidation(this.createSpecialtyFields);
          break;
        }
      }
    } else {
      this.setFormValidation(this.entityIdField);
    }
  }

  private setFormValidation(fields: string[]) {
    fields.forEach(field => {
      const control = this.rowActionForm.get(field);
      if (control) {
        switch (field) {
          case 'email': {
            control.setValidators([Validators.required, Validators.email]);
            control.updateValueAndValidity();
            break;
          }
          case 'password': {
            control.setValidators([Validators.required, Validators.minLength(6)]);
            control.updateValueAndValidity();
            break;
          }
          case 'pin': {
            control.setValidators([Validators.required, Validators.pattern('[0-9]{10}')]);
            control.updateValueAndValidity();
            break;
          }
          case 'specialtyId': {
            control.setValidators([Validators.required, Validators.min(1), Validators.pattern("^[0-9]*$")]);
            control.updateValueAndValidity();
            break;
          }
          case 'gpId': {
            control.setValidators([Validators.required, Validators.min(1), Validators.pattern("^[0-9]*$")]);
            control.updateValueAndValidity();
            break;
          }
          default: {
            control.setValidators([Validators.required]);
            control.updateValueAndValidity();
          }
        }
      }
    })
  }

  private resetAllFields(): void {
    this.allFields.forEach(field => {
      const control = this.rowActionForm.get(field);
      if (control) {
        control.clearValidators();
        control.setValue(null);
        control.updateValueAndValidity();
      }
    });
  }

  onSubmit() {
    if (this.rowActionForm.valid) {
      const formValue = this.rowActionForm.value;

      if (this.selectedAction.name === 'Delete' && this.selectedEntity === 'Patients') {
        this.usersService.deletePatient(formValue.entityId).subscribe({
          next: value => {
            this.rowActionSuccess = 'Patient deleted successfully';
            this.rowActionError = '';
          },
          error: err => {
            this.rowActionError = err.error.message;
            this.rowActionSuccess = '';
          }
        })
      }
    }
  }

  onActionSelected($event: Action) {
    this.selectedAction = $event;
  }
}
