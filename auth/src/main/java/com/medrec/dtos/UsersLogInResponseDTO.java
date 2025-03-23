package com.medrec.dtos;

public class UsersLogInResponseDTO {
    private boolean exists;
    private String email;
    private String password;

    public UsersLogInResponseDTO(boolean exists) {
        this.exists = exists;
    }

    public UsersLogInResponseDTO(boolean exists, String email, String password) {
        this.exists = exists;
        this.email = email;
        this.password = password;
    }

    public boolean getExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
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
}
