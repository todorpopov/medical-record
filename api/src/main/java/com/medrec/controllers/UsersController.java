package com.medrec.controllers;

import com.medrec.dtos.users.doctor.DoctorDTO;
import com.medrec.dtos.users.doctor.DoctorSummaryDTO;
import com.medrec.dtos.users.doctor.RegisterDoctorDTO;
import com.medrec.dtos.users.doctor.UpdateDoctorDTO;
import com.medrec.dtos.users.patient.PatientDTO;
import com.medrec.dtos.users.patient.RegisterPatientDTO;
import com.medrec.dtos.users.patient.UpdatePatientDTO;
import com.medrec.dtos.users.specialty.RegisterSpecialtyDTO;
import com.medrec.dtos.users.specialty.SpecialtyDTO;
import com.medrec.dtos.users.specialty.UpdateSpecialtyDTO;
import com.medrec.services.UsersService;
import com.medrec.utils.SuccessHTTPResponse;
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
    public ResponseEntity<SuccessHTTPResponse> deleteDoctorById(@RequestParam("id") int id) {
        this.logger.info(String.format("Deleting doctor by id %s endpoint called", id));
        this.usersService.deleteDoctorById(id);
        SuccessHTTPResponse response = new SuccessHTTPResponse("SUCCESS", "Doctor deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("patients/create")
    public ResponseEntity<PatientDTO> createPatient(@RequestBody RegisterPatientDTO dto) {
        this.logger.info("Creating new patient endpoint called");
        return ResponseEntity.ok(this.usersService.createPatient(dto));
    }

    @GetMapping("patients/get/id")
    public ResponseEntity<PatientDTO> getPatientById(@RequestParam("id") int id) {
        this.logger.info(String.format("Get patient by id %s endpoint called", id));
        return ResponseEntity.ok(this.usersService.getPatientById(id));
    }

    @GetMapping("patients/get/email")
    public ResponseEntity<PatientDTO> getPatientByEmail(@RequestParam("email") String email) {
        this.logger.info(String.format("Get patient by email %s endpoint called", email));
        return ResponseEntity.ok(this.usersService.getPatientByEmail(email));
    }

    @GetMapping("patients/list/all")
    public ResponseEntity<List<PatientDTO>> listAllPatients() {
        this.logger.info("List all patients endpoint called");
        return ResponseEntity.ok(this.usersService.getAllPatients());
    }

    @PutMapping("patients/update")
    public ResponseEntity<PatientDTO> updatePatient(@RequestBody UpdatePatientDTO dto) {
        this.logger.info("Updating patient endpoint called");
        return ResponseEntity.ok(this.usersService.updatePatient(dto));
    }

    @DeleteMapping("patients/delete")
    public ResponseEntity<SuccessHTTPResponse> deletePatientById(@RequestParam("id") int id) {
        this.logger.info(String.format("Deleting patient by id %s endpoint called", id));
        this.usersService.deletePatientById(id);
        SuccessHTTPResponse response = new SuccessHTTPResponse("SUCCESS", "Patient deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("specialty/create")
    public ResponseEntity<SpecialtyDTO> createSpecialty(@RequestBody RegisterSpecialtyDTO dto) {
        logger.info("Creating new specialty endpoint called");
        return ResponseEntity.ok(this.usersService.createSpecialty(dto));
    }

    @GetMapping("specialty/get/id")
    public ResponseEntity<SpecialtyDTO> getSpecialtyById(@RequestParam("id") int id) {
        logger.info(String.format("Get specialty by id %s endpoint called", id));
        return ResponseEntity.ok(this.usersService.getSpecialtyById(id));
    }

    @GetMapping("specialty/list/all")
    public ResponseEntity<List<SpecialtyDTO>> getAllSpecialties() {
        logger.info("Retrieving all specialties");
        return ResponseEntity.ok(this.usersService.getAllSpecialties());
    }

    @PutMapping("specialty/update")
    public ResponseEntity<SpecialtyDTO> updateSpecialty(@RequestBody UpdateSpecialtyDTO dto) {
        logger.info("Updating specialty endpoint called");
        return ResponseEntity.ok(this.usersService.updateSpecialty(dto));
    }

    @DeleteMapping("specialty/delete")
    public ResponseEntity<SuccessHTTPResponse> deleteSpecialtyById(@RequestParam("id") int id) {
        logger.info(String.format("Deleting specialty by id %s endpoint called", id));
        this.usersService.deleteSpecialtyById(id);
        SuccessHTTPResponse response = new SuccessHTTPResponse("SUCCESS", "Specialty deleted successfully");
        return ResponseEntity.ok(response);
    }
}
