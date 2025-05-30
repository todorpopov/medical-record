import {IcdDto} from './icd.dto';
import {SickLeaveDto} from './sick-leave.dto';

export interface DiagnosisDto {
  id: number,
  treatmentDescription: string,
  icd: IcdDto,
  sickLeave: SickLeaveDto | null,
}
