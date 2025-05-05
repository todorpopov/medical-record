package com.medrec.dtos.users.doctor;

import com.medrec.exception_handling.exceptions.IdNotSetException;
import com.medrec.grpc.users.Users;

public class UpdateDoctorDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean isGp;
    private Integer specialtyId;

    public UpdateDoctorDTO() {
    }

    public UpdateDoctorDTO(int id) {
        this.id = id;
    }

    public UpdateDoctorDTO(Integer id, String firstName, String lastName, String email, String password, Boolean isGp, Integer specialtyId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isGp = isGp;
        this.specialtyId = specialtyId;
    }

    public Users.UpdateDoctorRequest safeConvertToGrpcRequest() {
        Users.UpdateDoctorRequest.Builder requestBuilder = Users.UpdateDoctorRequest.newBuilder();

        if (this.id == null) {
            throw new IdNotSetException("");
        }

        requestBuilder.setDoctorId(this.id);

        if (this.firstName != null) {
            requestBuilder.setFirstName(this.firstName);
        }

        if (this.lastName != null) {
            requestBuilder.setLastName(this.lastName);
        }

        if (this.email != null) {
            requestBuilder.setEmail(this.email);
        }

        if (this.password != null) {
            requestBuilder.setPassword(this.password);
        }

        if (this.isGp != null) {
            requestBuilder.setIsGp(this.isGp);
        }

        if (this.specialtyId != null) {
            requestBuilder.setSpecialtyId(this.specialtyId);
        }

        return requestBuilder.build();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isGp() {
        return isGp;
    }

    public void setGp(boolean gp) {
        isGp = gp;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId = specialtyId;
    }

    @Override
    public String toString() {
        return "UpdateDoctorDTO{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", isGp=" + isGp +
            ", specialtyId=" + specialtyId +
            '}';
    }
}
