package com.medrec.services;

import com.medrec.dtos.*;
import com.medrec.gateways.AuthGateway;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthService {
    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    private final AuthGateway authGateway;

    public AuthService(AuthGateway authGateway) {
        this.authGateway = authGateway;
    }

    public AuthResponseDTO registerDoctor(RegisterDoctorDTO dto) {
        return this.authGateway.registerDoctor(
            dto.getFirstName(),
            dto.getLastName(),
            dto.getEmail(),
            dto.getPassword(),
            dto.isGeneralPractitioner(),
            dto.getSpecialtyId()
        );
    }

    public AuthResponseDTO registerPatient(RegisterPatientDTO dto) {
        this.logger.info(dto.getPassword());
        return this.authGateway.registerPatient(
            dto.getFirstName(),
            dto.getLastName(),
            dto.getEmail(),
            dto.getPassword(),
            dto.getPin(),
            dto.getGpId(),
            dto.isInsured()
        );
    }

    public AuthResponseDTO logDoctorIn(LogUserInDTO dto) {
        String token = authGateway.logDoctorIn(dto.getEmail(), dto.getPassword());
        return new AuthResponseDTO(
            token != null,
            token,
            "doctor"
        );
    }

    public AuthResponseDTO logPatientIn(LogUserInDTO dto) {
        String token = authGateway.logPatientIn(dto.getEmail(), dto.getPassword());
        return new AuthResponseDTO(
            token != null,
            token,
            "patient"
        );
    }

    public AuthResponseDTO logAdminIn(LogUserInDTO dto) {
        String token = authGateway.logAdminIn(dto.getEmail(), dto.getPassword());
        return new AuthResponseDTO(
            token != null,
            token,
            "admin"
        );
    }
}
