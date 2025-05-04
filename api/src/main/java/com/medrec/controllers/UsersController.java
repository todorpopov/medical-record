package com.medrec.controllers;

import com.medrec.dtos.DoctorSummaryDTO;
import com.medrec.dtos.SpecialtyDTO;
import com.medrec.services.UsersService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/users")
public class UsersController {
    private final Logger logger = Logger.getLogger(UsersController.class.getName());
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("specialty/all")
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        logger.info("Retrieving all specialties");
        return ResponseEntity.ok(this.usersService.getAllSpecialties());
    }

    @GetMapping("doctors/all-gp")
    public ResponseEntity<List<DoctorSummaryDTO>> getAllDoctors() {
        logger.info("Retrieving all GP doctors");
        return ResponseEntity.ok(this.usersService.getAllGpDoctors());
    }
}
