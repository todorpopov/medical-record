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

  constructor(
    private fb: FormBuilder,
    private usersService: UsersService,
  ) {
    this.rowActionForm = this.fb.group({
      action: [null, [Validators.required]],
      entityId: [null, [Validators.required]],

      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      firstName: [''],
      lastName: [''],

      gpId: [null],
      pin: ['', [Validators.required, Validators.pattern('[0-9]{10}')]],
      isHealthInsured: [true],

      specialtyId: [null],
      isGp: [true],

      specialtyName: [''],
      specialtyDescription: [''],
    });

    this.rowActionForm.get('action')?.valueChanges.subscribe(action => {
      this.onActionSelected(action);
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

  onActionSelected(action: Action): void {
    this.selectedAction = action
  }

  updateFormValidation() {
    const createPatientFields = ['firstName', 'lastName', 'email', 'password', 'gpId', 'pin', 'isHealthInsured'];
    const clearFieldsPatientCreate = ['entityId', 'specialtyId', 'isGp', 'specialtyName', 'specialtyDescription']

    const createDoctorFields = ['firstName', 'lastName', 'email', 'password', 'isGp', 'specialtyId'];
    const clearFieldsDoctorCreate = ['entityId', 'gpId', 'pin', 'specialtyName', 'specialtyDescription', 'isHealthInsured']

    const createSpecialtyFields = ['name', 'description'];
    const clearFieldsSpecialtyCreate = ['entityId', 'firstName', 'lastName', 'email', 'password', 'gpId', 'pin', 'isHealthInsured', 'specialtyId', 'isGp']

    const onlyId = ['entityId'];
    const allFieldsExceptId = ['name', 'description', 'firstName', 'lastName', 'email', 'password', 'gpId', 'pin', 'isHealthInsured', 'specialtyId', 'isGp']

    if (this.selectedAction.name === 'Create') {
      switch (this.selectedEntity) {
        case 'Patients': {
          this.setFormValidation(createPatientFields);
          this.clearForm(clearFieldsPatientCreate);
          break;
        }
        case 'Doctors': {
          this.setFormValidation(createDoctorFields);
          this.clearForm(clearFieldsDoctorCreate);
          break;
        }
        case 'Specialties': {
          this.setFormValidation(createSpecialtyFields);
          this.clearForm(clearFieldsSpecialtyCreate);
          break;
        }
      }
    } else {
      this.clearForm(allFieldsExceptId);
      this.setFormValidation(onlyId);
    }
  }

  private clearForm(fields: string[]) {
    for (const field of fields) {
      const control = this.rowActionForm.get(field);
      if (control) {
        control.clearValidators();
        control.updateValueAndValidity();
        control.setValue(null);
      }
    }
  }

  private setFormValidation(fields: string[]) {
    for (const field of fields) {
      const control = this.rowActionForm.get(field);
      if (control) {
        control.setValidators([Validators.required]);
        control.updateValueAndValidity();
      }
    }
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
}
