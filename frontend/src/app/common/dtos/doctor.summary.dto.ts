import { SpecialtyDTO } from "./specialty.dto";

export interface DoctorSummaryDTO {
    firstName: string,
    lastName: string,
    gp: boolean,
    specialty: SpecialtyDTO
}