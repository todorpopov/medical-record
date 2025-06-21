package com.medrec.controllers;

import com.medrec.dtos.queries.PatientCountDTO;
import com.medrec.dtos.users.patient.PatientDTO;
import com.medrec.services.GeneralWorkService;
import com.medrec.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("api/query")
public class QueryController {
    private final Logger logger = Logger.getLogger(QueryController.class.getName());
    private final UsersService usersService;
    private final GeneralWorkService generalWorkService;

    public QueryController(UsersService usersService, GeneralWorkService generalWorkService) {
        this.usersService = usersService;
        this.generalWorkService = generalWorkService;
    }

    @GetMapping("get-all-patients-for-gp/{id}")
    public ResponseEntity<List<PatientDTO>> getAllPatientsByGpId(@PathVariable("id") Integer id) {
        this.logger.info("Called endpoint Get All Patients for GP id " + id);
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(this.usersService.getAllPatientsByGpId(id));
    }

    @GetMapping("get-patients-count-for-gp-doctors")
    public ResponseEntity<List<PatientCountDTO>> countOfPatientsForDoctors() {
        this.logger.info("Called endpoint Get Patients Count For All GP Doctors");
        return ResponseEntity.ok(this.usersService.countOfPatientsForDoctors());
    }

    @GetMapping("get-patients-by-icd/{id}")
    public ResponseEntity<List<PatientDTO>> getPatientsByIcd(@PathVariable("id") Integer id) {
        this.logger.info("Called endpoint Get Patients By ICD id " + id);
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(this.generalWorkService.pipelinePatientsFromIcdId(id));
    }
}
