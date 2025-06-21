export type Page = 'dashboard' | 'login' | 'register' | 'menu' | 'home';

export type EntityType = 'Patients' | 'Doctors' | 'Specialties' | 'Appointments' | 'Diagnosis' |'ICD' | 'Sick Leave'

export type QueryType =
  'Patients By GP Id' |
  'Patients By ICD' |
  'Most Diagnosed ICD' |
  'Patients Count For All GP Doctors' |
  'Doctor Visits Count' |
  'Appointments For All Patients' |
  'Appointments For Period - All Doctors' |
  'Appointments For Period - Single Doctor' |
  'Most Sick Leaves - Month' |
  'Most Sick Leaves - Doctor'
