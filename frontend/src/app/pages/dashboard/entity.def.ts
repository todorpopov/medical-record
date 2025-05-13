import { ColDef } from 'ag-grid-community';
import {PatientSummary} from '../../common/interfaces/patient.dto';

export const patientsColumnDefs: ColDef<PatientSummary>[] = [
  {
    field: 'id',
    cellDataType: 'text',
  },
  {
    field: 'firstName',
    cellDataType: 'text',
  },
  {
    field: 'lastName',
    cellDataType: 'text',
  },
  {
    field: 'email',
    cellDataType: 'text',
  },
  {
    field: 'password',
    cellDataType: 'text',
  },
  {
    field: 'pin',
    headerName: 'PIN',
    cellDataType: 'text',
  },
  {
    field: 'gpId',
    headerName: 'General Practitioner Id',
    cellDataType: 'text',
  },
  {
    field: 'insured',
    headerName: 'Is Insured',
    editable: false,
    cellRenderer: (data: boolean) => {
      return data ? 'Yes' : 'No';
    }
  },
]

export const doctorColumnDefs: ColDef[] = [
  {
    field: 'id'
  },
  {
    field: 'firstName'
  },
  {
    field: 'lastName'
  },
  {
    field: 'email'
  },
  {
    field: 'password'
  },
  {
    field: 'specialtyId'
  },
  {
    field: 'gp',
    headerName: 'Is General Practitioner',
    cellRenderer: (data: boolean) => {
      return data ? 'Yes' : 'No';
    }
  },
]

export const specialtyColumnDefs: ColDef[] = [
  {
    field: 'id'
  },
  {
    field: 'name'
  },
  {
    field: 'description'
  },
]
