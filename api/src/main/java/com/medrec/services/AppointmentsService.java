package com.medrec.services;

import com.medrec.dtos.appointments.AppointmentDTO;
import com.medrec.dtos.appointments.CreateAppointmentDTO;
import com.medrec.dtos.appointments.UpdateAppointmentDTO;
import com.medrec.gateways.AppointmentsGateway;
import com.medrec.grpc.users.Appointments;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class AppointmentsService {
    private final Logger logger = Logger.getLogger(AppointmentsService.class.getName());

    private final AppointmentsGateway appointmentsGateway;

    public AppointmentsService(AppointmentsGateway appointmentsGateway) {
        this.appointmentsGateway = appointmentsGateway;
    }

    public AppointmentDTO createAppointment(CreateAppointmentDTO dto) throws RuntimeException {
        this.logger.info("Creating new appointment");

        Appointments.CreateAppointmentRequest request = Appointments.CreateAppointmentRequest.newBuilder()
            .setDate(dto.getDate())
            .setTime(dto.getTime())
            .setDoctorId(dto.getDoctorId())
            .setPatientId(dto.getPatientId())
            .build();

        try {
            Appointments.Appointment response = this.appointmentsGateway.createAppointment(request);
            AppointmentDTO appointmentDTO = getAppointmentDTO(response);

            this.logger.info("New appointment created with id: " + appointmentDTO.getId());
            return appointmentDTO;
        } catch (RuntimeException e) {
            this.logger.warning("Could not create new appointment");
            throw e;
        }
    }

    public AppointmentDTO getAppointmentById(int id) {
        this.logger.info("Retrieving appointment with id: " + id);

        try {
            Appointments.Appointment appointment = this.appointmentsGateway.getAppointmentById(id);
            AppointmentDTO appointmentDTO = getAppointmentDTO(appointment);

            this.logger.info("Appointment retrieved with id: " + appointmentDTO.getId());
            return appointmentDTO;
        } catch (RuntimeException e) {
            this.logger.warning("Could not retrieve appointment with id: " + id);
            throw e;
        }
    }

    public List<AppointmentDTO> getAllAppointments() {
        this.logger.info("Retrieving all appointments");

        try {
            Appointments.AppointmentsList appointments = this.appointmentsGateway.getAllAppointments();
            List<AppointmentDTO> appointmentDTOs = appointments.getAppointmentsList().stream()
                .map(AppointmentsService::getAppointmentDTO)
                .toList();

            this.logger.info(appointmentDTOs.size() + "appointments retrieved");
            return appointmentDTOs;
        } catch (RuntimeException e) {
            this.logger.warning("Could not retrieve all appointments");
            throw e;
        }
    }

    public List<AppointmentDTO> getAllByPatientEmail(String email) {
        this.logger.info("Retrieving all appointments for patient with email: " + email);

        try {
            Appointments.AppointmentsList appointments = this.appointmentsGateway.getAllByPatientEmail(email);
            List<AppointmentDTO> appointmentDTOs = appointments.getAppointmentsList().stream()
                .map(AppointmentsService::getAppointmentDTO)
                .toList();

            this.logger.info(appointmentDTOs.size() + "appointments retrieved for patient with email: " + email);
            return appointmentDTOs;
        } catch (RuntimeException e) {
            this.logger.warning("Could not retrieve all appointments for patient with email: " + email);
            throw e;
        }
    }

    public List<AppointmentDTO> getAllByDoctorEmail(String email) {
        this.logger.info("Retrieving all appointments for doctor with email: " + email);

        try {
            Appointments.AppointmentsList appointments = this.appointmentsGateway.getAllByDoctorEmail(email);
            List<AppointmentDTO> appointmentDTOs = appointments.getAppointmentsList().stream()
                .map(AppointmentsService::getAppointmentDTO)
                .toList();

            this.logger.info(appointmentDTOs.size() + "appointments retrieved for doctor with email: " + email);
            return appointmentDTOs;
        } catch (RuntimeException e) {
            this.logger.warning("Could not retrieve all appointments for doctor with email: " + email);
            throw e;
        }
    }

    public AppointmentDTO updateAppointment(UpdateAppointmentDTO dto) throws RuntimeException {
        this.logger.info("Updating appointment with id " + dto.getId());

        try {
            Appointments.Appointment updatedAppointment = this.appointmentsGateway.updateAppointment(dto.safeConvertToGrpcRequest());
            AppointmentDTO appointmentDTO = getAppointmentDTO(updatedAppointment);

            this.logger.info("Appointment updated with id: " + appointmentDTO.getId());
            return appointmentDTO;
        } catch (RuntimeException e) {
            this.logger.warning("Could not update appointment with id: " + dto.getId());
            throw e;
        }
    }

    public void deleteAppointmentById(int id) throws RuntimeException {
        this.logger.info("Deleting appointment with id: " + id);

        try {
            this.appointmentsGateway.deleteAppointment(id);
            this.logger.info("Appointment deleted with id: " + id);
        } catch (RuntimeException e) {
            this.logger.warning("Could not delete appointment with id: " + id);
            throw e;
        }
    }

    public void cascadeDeletePatientAppointments(int patientId) throws RuntimeException {
        this.logger.info("Deleting all appointments for patient with id: " + patientId);

        try {
            this.appointmentsGateway.cascadeDeletePatientAppointments(patientId);
            this.logger.info("All appointments for patient with id: " + patientId + " deleted");
        } catch (RuntimeException e) {
            this.logger.warning("Could not delete all appointments for patient with id: " + patientId);
            throw e;
        }
    }

    public void cascadeDeleteDoctorAppointments(int doctorId) throws RuntimeException {
        this.logger.info("Deleting all appointments for doctor with id: " + doctorId);

        try {
            this.appointmentsGateway.cascadeDeleteDoctorAppointments(doctorId);
            this.logger.info("All appointments for doctor with id: " + doctorId + " deleted");
        } catch (RuntimeException e) {
            this.logger.warning("Could not delete all appointments for doctor with id: " + doctorId);
            throw e;
        }
    }

    private static AppointmentDTO getAppointmentDTO(Appointments.Appointment appointment) {
        return new AppointmentDTO(
            appointment.getId(),
            appointment.getDate(),
            appointment.getTime(),
            appointment.getDoctorId(),
            appointment.getPatientId(),
            appointment.getStatus()
        );
    }
}
