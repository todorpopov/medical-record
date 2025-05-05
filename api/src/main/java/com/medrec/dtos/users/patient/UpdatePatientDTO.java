package com.medrec.dtos.users.patient;

import com.medrec.dtos.users.doctor.DoctorDTO;
import com.medrec.exception_handling.exceptions.IdNotSetException;
import com.medrec.grpc.users.Users;

public class UpdatePatientDTO {
    private Integer id;
    private String firstName;
    private String lastName;
    private String password;
    private Integer gpId;
    private Boolean insured;

    public UpdatePatientDTO() {
    }

    public UpdatePatientDTO(Integer id) {
        this.id = id;
    }

    public UpdatePatientDTO(Integer id, String firstName, String lastName, String password, Integer gpId, Boolean insured) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.gpId = gpId;
        this.insured = insured;
    }

    public Users.UpdatePatientRequest safeConvertToGrpcRequest() throws RuntimeException {
        Users.UpdatePatientRequest.Builder requestBuilder = Users.UpdatePatientRequest.newBuilder();

        if (this.id == null) {
            throw new IdNotSetException("");
        }

        requestBuilder.setPatientId(this.id);

        if (this.firstName != null) {
            requestBuilder.setFirstName(this.firstName);
        }

        if (this.lastName != null) {
            requestBuilder.setLastName(this.lastName);
        }

        if (this.password != null) {
            requestBuilder.setPassword(this.password);
        }

        if (this.gpId != null) {
            requestBuilder.setGpId(this.gpId);
        }

        if (this.insured != null) {
            requestBuilder.setIsHealthInsured(this.insured);
        }

        return requestBuilder.build();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGpId() {
        return gpId;
    }

    public void setGpId(Integer gpId) {
        this.gpId = gpId;
    }

    public Boolean getInsured() {
        return insured;
    }

    public void setInsured(Boolean insured) {
        this.insured = insured;
    }
}
