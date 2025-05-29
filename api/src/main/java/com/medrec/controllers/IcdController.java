package com.medrec.controllers;

import com.medrec.dtos.appointments.icd.CreateIcdDTO;
import com.medrec.dtos.appointments.icd.IcdDTO;
import com.medrec.dtos.appointments.icd.UpdateIcdDTO;
import com.medrec.services.AppointmentsService;
import com.medrec.utils.SuccessHTTPResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/icd")
public class IcdController {
    private final Logger logger = Logger.getLogger(IcdController.class.getName());
    private final AppointmentsService appointmentsService;

    @Autowired
    public IcdController(AppointmentsService appointmentsService) {
        this.appointmentsService = appointmentsService;
    }

    @PostMapping("create")
    public ResponseEntity<IcdDTO> createIcd(@RequestBody CreateIcdDTO dto) {
        this.logger.info("Creating new icd endpoint called");
        return ResponseEntity.ok(this.appointmentsService.createIcd(dto));
    }

    @GetMapping("get/id")
    public ResponseEntity<IcdDTO> getIcdById(@RequestParam("id") int id) {
        this.logger.info(String.format("Searching for icd by id %s endpoint called", id));
        return ResponseEntity.ok(this.appointmentsService.getIcdById(id));
    }

    @GetMapping("list/all")
    public ResponseEntity<List<IcdDTO>> getAllIdcEntities() {
        this.logger.info("List all icd endpoint called");
        return ResponseEntity.ok(this.appointmentsService.getAllIcds());
    }

    @PutMapping("update")
    public ResponseEntity<IcdDTO> updateIcd(@RequestBody UpdateIcdDTO dto) {
        this.logger.info("Updating icd endpoint called");
        return ResponseEntity.ok(this.appointmentsService.updateIcd(dto));
    }

    @DeleteMapping("delete")
    public ResponseEntity<SuccessHTTPResponse> deleteIcdById(@RequestParam("id") int id) {
        this.logger.info("Deleting icd by id " + id + "endpoint called");
        this.appointmentsService.deleteIcdById(id);
        SuccessHTTPResponse response = new SuccessHTTPResponse("SUCCESS", "ICD deleted successfully");
        return ResponseEntity.ok(response);
    }
}
