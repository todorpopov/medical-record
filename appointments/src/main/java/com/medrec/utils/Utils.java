package com.medrec.utils;

import com.medrec.dtos.icd.IcdOccurrenceDTO;
import com.medrec.exception_handling.exceptions.BadRequestException;
import com.medrec.grpc.appointments.Appointments;
import com.medrec.persistence.appointment.Appointment;
import com.medrec.persistence.diagnosis.Diagnosis;
import com.medrec.persistence.icd.Icd;
import com.medrec.persistence.leave.SickLeave;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Utils {
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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

    public static Appointments.Appointment getAppointmentFromDomainModel(Appointment appointment) {
        LocalDateTime dateTime = appointment.getDateTime();

        String date = dateTime.format(dateFormatter);
        String time = dateTime.format(timeFormatter);

        Appointments.Appointment.Builder builder = Appointments.Appointment.newBuilder();
        builder.setId(appointment.getId());
        builder.setDate(date);
        builder.setTime(time);
        builder.setStatus(appointment.getStatus());
        builder.setDoctorId(appointment.getDoctorId());
        builder.setPatientId(appointment.getPatientId());

        if (appointment.getDiagnosis() != null) {
            builder.setDiagnosis(getDiagnosisFromDomainModel(appointment.getDiagnosis()));
        }

        return builder.build();
    }

    public static Appointments.IcdOccurrence getIcdOccurrenceFromDto(IcdOccurrenceDTO dto) {
        return Appointments.IcdOccurrence.newBuilder()
            .setIcdId(dto.getIcdId())
            .setIcdCode(dto.getIcdCode())
            .setOccurrence(dto.getOccurrence())
            .build();
    }
}
