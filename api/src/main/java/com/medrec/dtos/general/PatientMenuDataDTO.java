package com.medrec.dtos.general;

import com.medrec.dtos.appointments.appointment.AppointmentDTO;
import com.medrec.dtos.users.doctor.DoctorDTO;

import java.util.List;

public class PatientMenuDataDTO {
    private List<DoctorDTO> doctors;
    private List<AppointmentDTO> appointments;

    public PatientMenuDataDTO() {
    }

    public PatientMenuDataDTO(List<DoctorDTO> doctors, List<AppointmentDTO> appointments) {
        this.doctors = doctors;
        this.appointments = appointments;
    }

    public List<DoctorDTO> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorDTO> doctors) {
        this.doctors = doctors;
    }

    public List<AppointmentDTO> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentDTO> appointments) {
        this.appointments = appointments;
    }
}
