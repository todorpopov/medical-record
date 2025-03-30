package com.medrec.dtos;

public class AuthResponseDTO {
    private boolean isSuccessful;
    private String token;
    private String role;

    public AuthResponseDTO(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public AuthResponseDTO(boolean isSuccessful, String token, String role) {
        this.isSuccessful = isSuccessful;
        this.token = token;
        this.role = role;
    }

    public AuthResponseDTO() {
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
