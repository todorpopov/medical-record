package com.medrec.services;

import com.medrec.dtos.auth.AuthResponseDTO;
import com.medrec.dtos.auth.LogUserInDTO;
import com.medrec.dtos.users.doctor.RegisterDoctorDTO;
import com.medrec.dtos.users.patient.RegisterPatientDTO;
import com.medrec.exception_handling.ExceptionsMapper;
import com.medrec.gateways.AuthGateway;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class AuthService {
    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    private final AuthGateway authGateway;

    public AuthService(AuthGateway authGateway) {
        this.authGateway = authGateway;
    }

    public AuthResponseDTO registerDoctor(RegisterDoctorDTO dto) throws RuntimeException {
        try {
            return this.authGateway.registerDoctor(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.isGeneralPractitioner(),
                dto.getSpecialtyId()
            );
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public AuthResponseDTO registerPatient(RegisterPatientDTO dto) throws RuntimeException {
        this.logger.info(dto.getPassword());
        try {
            return this.authGateway.registerPatient(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getPin(),
                dto.getGpId(),
                dto.isInsured()
            );
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public AuthResponseDTO logDoctorIn(LogUserInDTO dto) throws RuntimeException {
        try {
            String token = authGateway.logDoctorIn(dto.getEmail(), dto.getPassword());
            return new AuthResponseDTO(
                token != null,
                token,
                "doctor"
            );
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public AuthResponseDTO logPatientIn(LogUserInDTO dto) throws StatusRuntimeException {
        try {
            String token = authGateway.logPatientIn(dto.getEmail(), dto.getPassword());
            return new AuthResponseDTO(
                token != null,
                token,
                "patient"
            );
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public AuthResponseDTO logAdminIn(LogUserInDTO dto) throws StatusRuntimeException {
        try {
            String token = authGateway.logAdminIn(dto.getEmail(), dto.getPassword());
            return new AuthResponseDTO(
                token != null,
                token,
                "admin"
            );
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public boolean isRequestAuthorized(String token, List<String> requiredRoles) throws StatusRuntimeException {
        try {
            return authGateway.isRequestAuthorized(token, requiredRoles);
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }
}
