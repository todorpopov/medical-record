package com.medrec.dtos.users.specialty;

import com.medrec.grpc.users.Users;

public class UpdateSpecialtyDTO {
    private Integer id;
    private String name;
    private String description;

    public UpdateSpecialtyDTO() {
    }

    public UpdateSpecialtyDTO(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Users.UpdateSpecialtyRequest safeConvertToGrpcRequest() {
        Users.UpdateSpecialtyRequest.Builder requestBuilder = Users.UpdateSpecialtyRequest.newBuilder();

        if (this.id == null) {
            throw new RuntimeException("Specialty ID cannot be null");
        }

        requestBuilder.setId(this.id);

        if (this.name != null) {
            requestBuilder.setSpecialtyName(this.name);
        }

        if (this.description != null) {
            requestBuilder.setSpecialtyDescription(this.description);
        }

        return requestBuilder.build();
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
