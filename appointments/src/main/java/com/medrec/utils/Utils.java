package com.medrec.utils;

import com.medrec.exception_handling.exceptions.BadRequestException;
import com.medrec.grpc.users.Appointments;
import com.medrec.persistence.diagnosis.Diagnosis;
import com.medrec.persistence.icd.Icd;
import com.medrec.persistence.leave.SickLeave;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {
    public static LocalDate parseDate(String date) throws RuntimeException {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            throw new BadRequestException("date_time_not_parsed");
        }
    }

    public static Appointments.Diagnosis getDiagnosisFromDomainModel(Diagnosis diagnosis) {
        Appointments.Icd icd = getIcdFromDomainModel(diagnosis.getIcd());

        Appointments.Diagnosis.Builder builder = Appointments.Diagnosis.newBuilder();

        builder.setId(diagnosis.getId());
        builder.setTreatmentDescription(diagnosis.getTreatmentDescription());
        builder.setIcd(icd);

        if (diagnosis.getSickLeave() != null) {
            builder.setSickLeave(getSickLeaveFromDomainModel(diagnosis.getSickLeave()));
        }

        return builder.build();
    }

    public static Appointments.SickLeave getSickLeaveFromDomainModel(SickLeave sickLeave) {
        return Appointments.SickLeave.newBuilder()
            .setId(sickLeave.getId())
            .setDate(sickLeave.getStartDate().toString())
            .setDaysOfLeave(sickLeave.getDaysOfLeave())
            .build();
    }

    public static Appointments.Icd getIcdFromDomainModel(Icd icd) {
        return Appointments.Icd.newBuilder()
            .setId(icd.getId())
            .setCode(icd.getCode())
            .setDescription(icd.getDescription())
            .build();
    }
}
