package com.medrec.gateways;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import com.medrec.exception_handling.ExceptionsMapper;
import com.medrec.grpc.users.Appointments;
import com.medrec.grpc.users.AppointmentsServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class AppointmentsGateway {
    private final Logger logger = Logger.getLogger(AppointmentsGateway.class.getName());
    private final ManagedChannel channel;

    private final AppointmentsServiceGrpc.AppointmentsServiceBlockingStub appointmentsService;

    public AppointmentsGateway() {
        int port = Integer.parseInt(System.getenv("APPOINTMENTS_PORT"));
        String host = System.getenv("APPOINTMENTS_HOST");

        try {
            this.logger.info("Initializing Appointments Gateway with host: " + host + " and port: " + port);
            channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        } catch (Exception e) {
            this.logger.severe("Could not connect to Users Service");
            throw e;
        }

        appointmentsService = AppointmentsServiceGrpc.newBlockingStub(channel);
    }

    public Appointments.Appointment createAppointment(Appointments.CreateAppointmentRequest request) throws RuntimeException {
        try {
            return appointmentsService.createAppointment(request);
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Appointments.Appointment getAppointmentById(int id) throws RuntimeException {
        try {
            return appointmentsService.getAppointmentById(Int32Value.of(id));
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Appointments.AppointmentsList getAllAppointments() {
        try {
            return appointmentsService.getAllAppointments(Empty.getDefaultInstance());
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Appointments.AppointmentsList getAllByPatientEmail(String email) {
        try {
            return appointmentsService.getAllByPatientEmail(StringValue.of(email));
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Appointments.AppointmentsList getAllByDoctorEmail(String email) {
        try {
            return appointmentsService.getAllByDoctorEmail(StringValue.of(email));
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public Appointments.Appointment updateAppointment(Appointments.UpdateAppointmentRequest request) throws RuntimeException {
        try {
            return appointmentsService.updateAppointment(request);
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public void deleteAppointment(int id) throws RuntimeException {
        try {
            Empty result = appointmentsService.deleteAppointmentById(Int32Value.of(id));
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public void cascadeDeletePatientAppointments(int patientId) throws RuntimeException {
        try {
            Empty result = appointmentsService.cascadeDeletePatientAppointments(Int32Value.of(patientId));
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }

    public void cascadeDeleteDoctorAppointments(int doctorId) throws RuntimeException {
        try {
            Empty result = appointmentsService.cascadeDeleteDoctorAppointments(Int32Value.of(doctorId));
        } catch (StatusRuntimeException e) {
            throw ExceptionsMapper.translateStatusRuntimeException(e);
        }
    }
}
