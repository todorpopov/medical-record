export type Page = 'dashboard' | 'login' | 'register' | 'menu' | 'home';

export type EntityType = 'Patients' | 'Doctors' | 'Specialties' | 'Appointments' | 'Diagnosis' |'ICD' | 'Sick Leave'

export type QueryType =
  'Patients By GP Id' |
  'Occurrence Of ICD Diagnoses' |
  'Patients By ICD Diagnosis' |
  'Patients Count For All GP Doctors' |
  'Doctor Visits Count' |
  'Appointments For All Patients' |
  'Appointments For Period - All Doctors' |
  'Appointments For Period - Single Doctor' |
  'Most Sick Leaves - Month' |
  'Most Sick Leaves - Doctor'

export type FrequencyLimitType =
  { text: '1', value: 1} |
  { text: '2', value: 2} |
  { text: '3', value: 3} |
  { text: '4', value: 4} |
  { text: '5', value: 5}
