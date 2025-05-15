import {Component, Input, OnChanges} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {DropdownComponent} from '../dropdown/dropdown.component';
import {NgIf} from '@angular/common';
import {TextInputComponent} from '../text-input/text-input.component';
import {CheckboxComponent} from '../checkbox/checkbox.component';
import {UsersService} from '../../services/users.service';
import {RegisterPatientDto, UpdatePatientDto} from '../../common/dtos/patient.dto';
import {AuthService} from '../../services/auth.service';
import {RegisterDoctorDto, UpdateDoctorDto} from '../../common/dtos/doctor.dto';
import {CreateSpecialtyDto, UpdateSpecialtyDto} from '../../common/dtos/specialty.dto';

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
    private authService: AuthService,
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

  onActionSelected($event: Action) {
    this.selectedAction = $event;
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

      const entityId = formValue.entityId;

      const firstName = formValue.firstName;
      const lastName = formValue.lastName;

      const email = formValue.email;
      const password = formValue.password;

      const pin = formValue.pin;
      const gpId = formValue.gpId;
      const isHealthInsured = formValue.isHealthInsured;
      const isGp = formValue.isGp;
      const specialtyId = formValue.specialtyId;

      switch (this.selectedEntity) {
        case 'Patients': {
          switch (this.selectedAction.name) {
            case 'Create': {
              const dto: RegisterPatientDto = {
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password,
                pin: pin,
                gpId: gpId,
                isInsured: isHealthInsured
              }

              this.handlePatientCreate(dto);
              break;
            }

            case 'Update': {
              const dto: UpdatePatientDto = {
                id: entityId,
                firstName: firstName,
                lastName: lastName,
                gpId: gpId,
                isInsured: isHealthInsured
              }

              this.handlePatientUpdate(dto);
              break;
            }

            case 'Delete': {
              this.handlePatientDelete(entityId);
              break;
            }
          }

          break;
        }
        case 'Doctors': {
          switch (this.selectedAction.name) {
            case 'Create': {
              const dto: RegisterDoctorDto = {
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password,
                generalPractitioner: isGp,
                specialtyId: specialtyId
              }

              this.handleDoctorCreate(dto);
              break;
            }

            case 'Update': {
              const dto: UpdateDoctorDto = {
                id: entityId,
                firstName: firstName,
                lastName: lastName,
                specialtyId: specialtyId,
                generalPractitioner: isGp
              }

              this.handleDoctorUpdate(dto);
              break;
            }

            case 'Delete': {
              this.handleDoctorDelete(entityId);
              break;
            }
          }

          break;
        }

        case 'Specialties': {
          switch (this.selectedAction.name) {
            case 'Create': {
              const dto: CreateSpecialtyDto = {
                name: formValue.specialtyName,
                description: formValue.specialtyDescription
              }

              this.handleCreateSpecialty(dto);
              break;
            }

            case 'Update': {
              const dto: UpdateSpecialtyDto = {
                id: entityId,
                name: formValue.specialtyName,
                description: formValue.specialtyDescription
              }

              this.handleUpdateSpecialty(dto);
              break;
            }

            case 'Delete': {
              this.handleDeleteSpecialty(entityId);
              break;
            }
          }
        }
      }
    }
  }

  private handlePatientCreate(dto: RegisterPatientDto): void {
    this.authService.registerPatient(
      dto.firstName,
      dto.lastName,
      dto.email,
      dto.password,
      dto.pin,
      dto.gpId,
      dto.isInsured
    ).subscribe({
      next: () => {
        this.rowActionSuccess = 'Patient created successfully';
        this.rowActionError = '';
      },
      error: (error) => {
        this.rowActionError = error.error.message;
        this.rowActionSuccess = '';
      }
    })
  }

  private handlePatientUpdate(dto: UpdatePatientDto): void {
    this.usersService.updatePatient(
      dto.id,
      dto.firstName,
      dto.lastName,
      dto.gpId,
      dto.isInsured,
    ).subscribe({
      next: () => {
        this.rowActionSuccess = 'Patient updated successfully';
        this.rowActionError = '';
      },
      error: (error) => {
        this.rowActionError = error.error.message;
        this.rowActionSuccess = '';
      }
    })
  }

  private handlePatientDelete(id: number): void {
    this.usersService.deletePatient(id).subscribe({
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

  private handleDoctorCreate(dto: RegisterDoctorDto): void {
    this.authService.registerDoctor(
      dto.firstName,
      dto.lastName,
      dto.email,
      dto.password,
      dto.generalPractitioner,
      dto.specialtyId,
    ).subscribe({
      next: () => {
        this.rowActionSuccess = 'Doctor created successfully';
        this.rowActionError = '';
      },
      error: (error) => {
        this.rowActionError = error.error.message;
        this.rowActionSuccess = '';
      }
    })
  }

  private handleDoctorUpdate(dto: UpdateDoctorDto): void {
    this.usersService.updateDoctor(
      dto.id,
      dto.firstName,
      dto.lastName,
      dto.specialtyId,
      dto.generalPractitioner,
    ).subscribe({
      next: () => {
        this.rowActionSuccess = 'Doctor updated successfully';
        this.rowActionError = '';
      },
      error: (error) => {
        this.rowActionError = error.error.message;
        this.rowActionSuccess = '';
      }
    })
  }

  private handleDoctorDelete(id: number): void {
    this.usersService.deleteDoctor(id).subscribe({
      next: value => {
        this.rowActionSuccess = 'Doctor deleted successfully';
        this.rowActionError = '';
      },
      error: err => {
        this.rowActionError = err.error.message;
        this.rowActionSuccess = '';
      }
    })
  }

  private handleCreateSpecialty(dto: CreateSpecialtyDto): void {
    this.usersService.createSpecialty(dto.name, dto.description).subscribe({
      next: value => {
        this.rowActionSuccess = 'Specialty created successfully';
        this.rowActionError = '';
      },
      error: err => {
        this.rowActionError = err.error.message;
        this.rowActionSuccess = '';
      }
    })
  }

  private handleUpdateSpecialty(dto: UpdateSpecialtyDto): void {
    this.usersService.updateSpecialty(dto.id, dto.name, dto.description).subscribe({
      next: value => {
        this.rowActionSuccess = 'Specialty updated successfully';
        this.rowActionError = '';
      },
      error: err => {
        this.rowActionError = err.error.message;
        this.rowActionSuccess = '';
      }
    })
  }

  private handleDeleteSpecialty(id: number): void {
    this.usersService.deleteSpecialty(id).subscribe({
      next: value => {
        this.rowActionSuccess = 'Specialty deleted successfully';
        this.rowActionError = '';
      },
      error: err => {
        this.rowActionError = err.error.message;
        this.rowActionSuccess = '';
      }
    })
  }
}
