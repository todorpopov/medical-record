package com.medrec.controllers;

import com.medrec.dtos.DoctorSummaryDTO;
import com.medrec.dtos.SpecialtyDTO;
import com.medrec.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("specialty/all")
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        return ResponseEntity.ok(this.usersService.getAllSpecialties());
    }

    @GetMapping("doctors/all-gp")
    public ResponseEntity<List<DoctorSummaryDTO>> getAllDoctors() {
        return ResponseEntity.ok(this.usersService.getAllGpDoctors());
    }
}
