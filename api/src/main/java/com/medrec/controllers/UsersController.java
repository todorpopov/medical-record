package com.medrec.controllers;

import com.medrec.dtos.users.doctor.DoctorDTO;
import com.medrec.dtos.users.doctor.DoctorSummaryDTO;
import com.medrec.dtos.users.doctor.RegisterDoctorDTO;
import com.medrec.dtos.users.doctor.UpdateDoctorDTO;
import com.medrec.dtos.users.specialty.SpecialtyDTO;
import com.medrec.services.UsersService;
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

    @PostMapping("doctors/create")
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody RegisterDoctorDTO dto) {
        this.logger.info("Creating new doctor endpoint called");
        return ResponseEntity.ok(this.usersService.createDoctor(dto));
    }

    @GetMapping("doctors/get/id")
    public ResponseEntity<DoctorDTO> getDoctorById(@RequestParam("id") int id) {
        this.logger.info(String.format("Get doctor by id %s endpoint called", id));
        return ResponseEntity.ok(this.usersService.getDoctorById(id));
    }

    @GetMapping("doctors/get/email")
    public ResponseEntity<DoctorDTO> getDoctorByEmail(@RequestParam("email") String email) {
        this.logger.info(String.format("Get doctor by email %s endpoint called", email));
        return ResponseEntity.ok(this.usersService.getDoctorByEmail(email));
    }

    @GetMapping("doctors/list/all")
    public ResponseEntity<List<DoctorDTO>> listAllDoctors() {
        this.logger.info("List all doctors endpoint called");
        return ResponseEntity.ok(this.usersService.getAllDoctors());
    }

    @GetMapping("doctors/list/gp")
    public ResponseEntity<List<DoctorSummaryDTO>> listAllGpDoctors() {
        logger.info("List all GP doctors endpoint called");
        return ResponseEntity.ok(this.usersService.getAllGpDoctors());
    }

    @PutMapping("doctors/update")
    public ResponseEntity<DoctorDTO> updateDoctor(@RequestBody UpdateDoctorDTO dto) {
        this.logger.info("Updating doctor endpoint called");
        return ResponseEntity.ok(this.usersService.updateDoctor(dto));
    }

    @DeleteMapping("doctors/delete")
    public ResponseEntity<Void> deleteDoctorById(@RequestParam("id") int id) {
        this.logger.info(String.format("Deleting doctor by id %s endpoint called", id));
        this.usersService.deleteDoctorById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("specialty/all")
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        logger.info("Retrieving all specialties");
        return ResponseEntity.ok(this.usersService.getAllSpecialties());
    }
}
