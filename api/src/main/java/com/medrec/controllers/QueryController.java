package com.medrec.controllers;

import com.medrec.dtos.appointments.appointment.AppointmentsByPatientDTO;
import com.medrec.dtos.appointments.appointment.DoctorAppointmentsCountDTO;
import com.medrec.dtos.appointments.icd.IcdOccurrenceDTO;
import com.medrec.dtos.queries.PatientCountDTO;
import com.medrec.dtos.users.patient.PatientDTO;
import com.medrec.services.AppointmentsService;
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
    private final AppointmentsService appointmentsService;

    public QueryController(UsersService usersService, GeneralWorkService generalWorkService, AppointmentsService appointmentsService) {
        this.usersService = usersService;
        this.generalWorkService = generalWorkService;
        this.appointmentsService = appointmentsService;
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

    @GetMapping("get-most-frequent-icds/{limit}")
    public ResponseEntity<List<IcdOccurrenceDTO>> getMostFrequentIcds(@PathVariable("limit") Integer limit) {
        this.logger.info("Called endpoint Get Most Frequent Icd Occurrences With Limit");
        if (limit == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(this.appointmentsService.mostFrequentIcds(limit));
    }

    @GetMapping("get-doctor-appointments-count")
    public ResponseEntity<List<DoctorAppointmentsCountDTO>> getDoctorAppointmentsCount() {
        this.logger.info("Getting doctor appointments count endpoint called");
        return ResponseEntity.ok(this.appointmentsService.getDoctorAppointmentsCount());
    }

    @GetMapping("list-appointments-by-patient")
    public ResponseEntity<List<AppointmentsByPatientDTO>> listAppointmentsByPatient() {
        this.logger.info("Called endpoint Get Appointments By Patient");
        return ResponseEntity.ok(this.appointmentsService.getAppointmentsByPatient());
    }
}
