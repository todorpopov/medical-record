package com.medrec.controllers;

import com.medrec.dtos.auth.AuthResponseDTO;
import com.medrec.dtos.auth.LogUserInDTO;
import com.medrec.dtos.users.doctor.RegisterDoctorDTO;
import com.medrec.dtos.users.patient.RegisterPatientDTO;
import com.medrec.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final Logger logger = Logger.getLogger(AuthController.class.getName());
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register-doctor")
    public ResponseEntity<AuthResponseDTO> registerDoctor(@RequestBody RegisterDoctorDTO dto) {
        logger.info(dto.toString());
        AuthResponseDTO response = this.authService.registerDoctor(dto);
        if (response.isSuccessful()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("register-patient")
    public ResponseEntity<AuthResponseDTO> registerPatient(@RequestBody RegisterPatientDTO dto) {
        logger.info(dto.toString());
        AuthResponseDTO response = this.authService.registerPatient(dto);
        if (response.isSuccessful()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("log-doctor-in")
    public ResponseEntity<AuthResponseDTO> logDoctorIn(@RequestBody LogUserInDTO dto) {
        logger.info("Log Doctor In: " + dto.getEmail());
        return ResponseEntity.ok(this.authService.logDoctorIn(dto));
    }

    @PostMapping("log-patient-in")
    public ResponseEntity<AuthResponseDTO> logPatientIn(@RequestBody LogUserInDTO dto) {
        logger.info("Log Patient In: " + dto.getEmail());
        AuthResponseDTO response = this.authService.logPatientIn(dto);
        if (response.isSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PostMapping("log-admin-in")
    public ResponseEntity<AuthResponseDTO> logAdminIn(@RequestBody LogUserInDTO dto) {
        logger.info("Log Admin In: " + dto.getEmail());
        AuthResponseDTO responseDTO = this.authService.logAdminIn(dto);
        if (responseDTO.isSuccessful()) {
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }
}
