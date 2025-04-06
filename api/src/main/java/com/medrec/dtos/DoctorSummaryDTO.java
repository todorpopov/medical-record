package com.medrec.dtos;

public class DoctorSummaryDTO {
    private String firstName;
    private String lastName;
    private boolean isGp;
    private SpecialtyDTO specialty;

    public DoctorSummaryDTO() {
    }

    public DoctorSummaryDTO(String firstName, String lastName, boolean isGp, SpecialtyDTO specialty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isGp = isGp;
        this.specialty = specialty;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isGp() {
        return isGp;
    }

    public void setGp(boolean gp) {
        isGp = gp;
    }

    public SpecialtyDTO getSpecialty() {
        return specialty;
    }

    public void setSpecialty(SpecialtyDTO specialty) {
        this.specialty = specialty;
    }
}
