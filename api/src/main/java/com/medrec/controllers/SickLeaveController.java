package com.medrec.controllers;

import com.medrec.dtos.appointments.sick_leave.CreateSickLeaveDTO;
import com.medrec.dtos.appointments.sick_leave.SickLeaveDTO;
import com.medrec.dtos.appointments.sick_leave.UpdateSickLeaveDTO;
import com.medrec.services.AppointmentsService;
import com.medrec.utils.SuccessHTTPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/sick-leave")
public class SickLeaveController {
    private final Logger logger = Logger.getLogger(SickLeaveController.class.getName());
    private final AppointmentsService appointmentsService;

    @Autowired
    public SickLeaveController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    @PostMapping("create")
    public ResponseEntity<SickLeaveDTO> createSickLeave(@RequestBody CreateSickLeaveDTO dto) {
        this.logger.info("Creating new sick leave");
        return ResponseEntity.ok(this.appointmentsService.createSickLeave(dto));
    }

    @GetMapping("get/id")
    public ResponseEntity<SickLeaveDTO> getSickLeaveById(@RequestParam("id") int id) {
        this.logger.info("Get sick leave by id " + id + " endpoint called");
        return ResponseEntity.ok(this.appointmentsService.getSickLeaveById(id));
    }

    @GetMapping("list/all")
    public ResponseEntity<List<SickLeaveDTO>> getAllSickLeaveEntities() {
        this.logger.info("Get all sick leave entities endpoint called");
        return ResponseEntity.ok(this.appointmentsService.getAllSickLeaveEntities());
    }

    @PutMapping("update")
    public ResponseEntity<SickLeaveDTO> updateSickLeave(@RequestBody UpdateSickLeaveDTO dto) {
        this.logger.info("Updating sick leave endpoint called");
        return ResponseEntity.ok(this.appointmentsService.updateSickLeave(dto));
    }

    @DeleteMapping("delete")
    public ResponseEntity<SuccessHTTPResponse> deleteSickLeaveById(@RequestParam("id") int id) {
        this.logger.info("Deleting sick leave by id " + id + "endpoint called");
        this.appointmentsService.deleteSickLeaveById(id);
        SuccessHTTPResponse response = new SuccessHTTPResponse("SUCCESS", "Sick Leave deleted successfully");
        return ResponseEntity.ok(response);
    }
}
