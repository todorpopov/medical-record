export type Page = 'dashboard' | 'login' | 'register' | 'menu' | 'home' | 'admin-query';

export type EntityType = 'Patients' | 'Doctors' | 'Specialties' | 'Appointments' | 'Diagnosis' |'ICD' | 'Sick Leave'

export type QueryType =
  'Patients By GP' |
  'Occurrence Of ICD Diagnoses' |
  'Patients By ICD Diagnosis' |
  'Patients Count For All GP Doctors' |
  'Doctor Appointments Count' |
  'List Appointments By Patients' |
  'List Appointments By Time Period' |
  'List Appointments By Time Period And Doctor' |
  'Most Sick Leaves - Month' |
  'Most Sick Leaves - Doctor'

export type FrequencyLimitType =
  { text: '1', value: 1} |
  { text: '2', value: 2} |
  { text: '3', value: 3} |
  { text: '4', value: 4} |
  { text: '5', value: 5}
