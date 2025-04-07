import { Specialty } from "./specialty";

export interface DoctorSummary {
  id: number,
  firstName: string,
  lastName: string,
  fullName: string,
  gp: boolean,
  specialty: Specialty
}
