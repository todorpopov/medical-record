package com.medrec.controllers;

import com.medrec.dtos.appointments.AppointmentDTO;
import com.medrec.dtos.appointments.CreateAppointmentDTO;
import com.medrec.dtos.appointments.UpdateAppointmentDTO;
import com.medrec.services.AppointmentsService;
import com.medrec.utils.SuccessHTTPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/appointments")
public class AppointmentsController {
    private final Logger logger = Logger.getLogger(AppointmentsController.class.getName());
    private final AppointmentsService appointmentsService;

    @Autowired
    public AppointmentsController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    @PostMapping("create")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody CreateAppointmentDTO dto) {
        this.logger.info("Creating new appointment endpoint called");
        return ResponseEntity.ok(this.appointmentsService.createAppointment(dto));
    }

    @GetMapping("get/id")
    public ResponseEntity<AppointmentDTO> getAppointmentById(@RequestParam("id") int id) {
        this.logger.info(String.format("Searching for appointment by id %s endpoint called", id));
        return ResponseEntity.ok(this.appointmentsService.getAppointmentById(id));
    }

    @GetMapping("list/all")
    public ResponseEntity<List<AppointmentDTO>> listAllAppointments() {
        this.logger.info("List all appointments endpoint called");
        return ResponseEntity.ok(this.appointmentsService.getAllAppointments());
    }

    @GetMapping("list/all/patient-email")
    public ResponseEntity<List<AppointmentDTO>> listAllByPatientEmail(@RequestParam("email") String email) {
        this.logger.info(String.format("List all appointments by patient email %s endpoint called", email));
        return ResponseEntity.ok(this.appointmentsService.getAllByPatientEmail(email));
    }

    @GetMapping("list/all/doctor-email")
    public ResponseEntity<List<AppointmentDTO>> listAllByDoctorEmail(@RequestParam("email") String email) {
        this.logger.info(String.format("List all appointments by doctor email %s endpoint called", email));
        return ResponseEntity.ok(this.appointmentsService.getAllByDoctorEmail(email));
    }

    @PutMapping("update")
    public ResponseEntity<AppointmentDTO> updateAppointment(@RequestBody UpdateAppointmentDTO dto) {
        this.logger.info(String.format("Updating appointment with id %s endpoint called", dto.getId()));
        return ResponseEntity.ok(this.appointmentsService.updateAppointment(dto));
    }

    @DeleteMapping("delete")
    public ResponseEntity<SuccessHTTPResponse> deleteDoctorById(@RequestParam("id") int id) {
        this.logger.info("Deleting appointment by id " + id + "endpoint called");
        this.appointmentsService.deleteAppointmentById(id);
        SuccessHTTPResponse response = new SuccessHTTPResponse("SUCCESS", "Appointment deleted successfully");
        return ResponseEntity.ok(response);
    }
}
