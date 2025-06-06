package com.medrec.services;

import com.medrec.dtos.appointments.appointment.AppointmentDTO;
import com.medrec.dtos.general.PatientMenuDataDTO;
import com.medrec.dtos.users.doctor.DoctorDTO;
import com.medrec.exception_handling.exceptions.ConcurrentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.logging.Logger;

@Service
public class GeneralWorkService {
    private final Logger logger = Logger.getLogger(GeneralWorkService.class.getName());
    private final Executor executor;
    private final AppointmentsService appointmentsService;
    private final UsersService usersService;

    @Autowired
    public GeneralWorkService(@Qualifier("executor") Executor executor, AppointmentsService appointmentsService, UsersService usersService) {
        this.executor = executor;
        this.appointmentsService = appointmentsService;
        this.usersService = usersService;
    }

    public PatientMenuDataDTO getPatientMenuData(int patientId) throws RuntimeException {
        CompletableFuture<List<DoctorDTO>> doctorsFuture = CompletableFuture.supplyAsync(() -> {
            String threadName = Thread.currentThread().getName();
            this.logger.info(String.format("Getting all doctors for patient menu (patient - %d) on thread (%s)", patientId, threadName));

            try {
                List<DoctorDTO> doctors = this.usersService.getAllDoctors();
                this.logger.info("Found " + doctors.size() + " doctors");
                return doctors;
            } catch (Exception e) {
                this.logger.warning(e.getMessage());
                throw new ConcurrentException("Error getting doctors for patient menu");
            }
        }, executor);

        CompletableFuture<List<AppointmentDTO>> appointmentsFuture = CompletableFuture.supplyAsync(() -> {
            String threadName = Thread.currentThread().getName();
            this.logger.info(String.format("Getting all appointments for patient menu (patient - %d) on thread (%s)", patientId, threadName));

            try {
                List<AppointmentDTO> appointments = this.appointmentsService.getAllByPatientId(patientId);
                this.logger.info("Found " + appointments.size() + " appointments");
                return appointments;
            } catch (Exception e) {
                this.logger.warning(e.getMessage());
                throw new ConcurrentException("Error getting appointments for patient menu");
            }
        }, executor);

        CompletableFuture<Void> done = CompletableFuture.allOf(doctorsFuture, appointmentsFuture);

        done.join();

        try {
            List<DoctorDTO> doctors = doctorsFuture.get();
            List<AppointmentDTO> appointments = appointmentsFuture.get();
            this.logger.info(String.format("Patient Menu Data: (doctors - %d, appointments - %d)", doctors.size(), appointments.size()));

            return new PatientMenuDataDTO(
                doctors,
                appointments
            );
        } catch (ExecutionException | InterruptedException e) {
            this.logger.severe(e.getMessage());
            throw new ConcurrentException("Error getting patient menu");
        }
    }
}
