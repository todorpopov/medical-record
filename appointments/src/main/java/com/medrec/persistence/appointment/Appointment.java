package com.medrec.persistence.appointment;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "datetime", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "doctor_id", nullable = false)
    private int doctorId;

    @Column(name = "patient_id", nullable = false)
    private int patientId;

    public Appointment() {
    }

    public Appointment(LocalDateTime dateTime, int patientId, int doctorId, String status) {
        this.dateTime = dateTime;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    @Override
    public String toString() {
        return "Appointment{" +
            "id=" + id +
            ", dateTime=" + dateTime +
            ", status='" + status + '\'' +
            ", doctorId=" + doctorId +
            ", patientId=" + patientId +
            '}';
    }
}
