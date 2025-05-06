package com.medrec.dtos.auth;

public class AuthResponseDTO {
    private String token;
    private String role;

    public AuthResponseDTO(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public AuthResponseDTO() {
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
