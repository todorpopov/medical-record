package com.medrec.services;

import com.google.protobuf.Empty;
import com.google.protobuf.Int32Value;
import com.google.protobuf.StringValue;
import com.medrec.exception_handling.ExceptionsMapper;
import com.medrec.grpc.users.Appointments;
import com.medrec.grpc.users.AppointmentsServiceGrpc;
import com.medrec.persistence.appointment.Appointment;
import com.medrec.persistence.appointment.AppointmentsRepository;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Logger;

public class AppointmentsService extends AppointmentsServiceGrpc.AppointmentsServiceImplBase {
    private static AppointmentsService instance;

    private final Logger logger = Logger.getLogger(AppointmentsService.class.getName());

    private final AppointmentsRepository appointmentsRepository = AppointmentsRepository.getInstance();

    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private AppointmentsService() {}

    public static AppointmentsService getInstance() {
        if (instance == null) {
            instance = new AppointmentsService();
        }
        return instance;
    }

    @Override
    public void createAppointment(Appointments.CreateAppointmentRequest request, StreamObserver<Appointments.Appointment> responseObserver) {
        this.logger.info("Called RPC Create Appointment");

        try {
            Appointment appointment = this.appointmentsRepository.save(
                request.getDate(),
                request.getTime(),
                request.getDoctorId(),
                request.getPatientId()
            );

            responseObserver.onNext(grpcFromDomainModel(appointment));
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        }
    }

    @Override
    public void getAppointmentById(Int32Value request, StreamObserver<Appointments.Appointment> responseObserver) {
        int id = request.getValue();
        this.logger.info("Called RPC Get Appointment By Id: " + id);

        try {
            Appointment appointment = this.appointmentsRepository.getById(id);
            responseObserver.onNext(grpcFromDomainModel(appointment));
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        }
    }

    @Override
    public void getAllAppointments(Empty request, StreamObserver<Appointments.AppointmentsList> responseObserver) {
        this.logger.info("Called RPC Get All Appointments");

        try {
            List<Appointment> appointments = this.appointmentsRepository.findAll();
            List<Appointments.Appointment> grpcAppointments = appointments.stream()
                .map(AppointmentsService::grpcFromDomainModel)
                .toList();

            Appointments.AppointmentsList list = Appointments.AppointmentsList.newBuilder()
                .addAllAppointments(grpcAppointments)
                .build();

            responseObserver.onNext(list);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        }
    }

    @Override
    public void getAllByPatientEmail(StringValue request, StreamObserver<Appointments.AppointmentsList> responseObserver) {
        String email = request.getValue();
        this.logger.info("Called RPC Get All Appointments By Patient Email: " + email);

        try {
            List<Appointment> appointments = this.appointmentsRepository.findAllByPatientEmail(email);
            List<Appointments.Appointment> grpcAppointments = appointments.stream()
                .map(AppointmentsService::grpcFromDomainModel)
                .toList();

            Appointments.AppointmentsList list = Appointments.AppointmentsList.newBuilder()
                .addAllAppointments(grpcAppointments)
                .build();

            responseObserver.onNext(list);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        }
    }

    @Override
    public void getAllByDoctorEmail(StringValue request, StreamObserver<Appointments.AppointmentsList> responseObserver) {
        String email = request.getValue();
        this.logger.info("Called RPC Get All Appointments By Doctor Email: " + email);

        try {
            List<Appointment> appointments = this.appointmentsRepository.findAllByDoctorEmail(email);
            List<Appointments.Appointment> grpcAppointments = appointments.stream()
                .map(AppointmentsService::grpcFromDomainModel)
                .toList();

            Appointments.AppointmentsList list = Appointments.AppointmentsList.newBuilder()
                .addAllAppointments(grpcAppointments)
                .build();

            responseObserver.onNext(list);
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        }
    }

    @Override
    public void updateAppointment(Appointments.UpdateAppointmentRequest request, StreamObserver<Appointments.Appointment> responseObserver) {
        int id = request.getId();
        String status = request.getStatus();

        this.logger.info(String.format("Called RPC Update Appointment for id (%s) update status to (%s)", id, status));

        try {
            Appointment appointment = this.appointmentsRepository.update(id, status);
            responseObserver.onNext(grpcFromDomainModel(appointment));
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        }
    }

    @Override
    public void deleteAppointmentById(Int32Value request, StreamObserver<Empty> responseObserver) {
        int id = request.getValue();
        this.logger.info("Called RPC Delete Appointment By Id: " + id);

        try {
            this.appointmentsRepository.delete(id);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        }
    }

    @Override
    public void cascadeDeletePatientAppointments(Int32Value request, StreamObserver<Empty> responseObserver) {
        int id = request.getValue();
        this.logger.info("Cascade deleting all appointments for patient with id: " + id);

        try {
            this.appointmentsRepository.genericCascadeDelete(id, "patient");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        }
    }

    @Override
    public void cascadeDeleteDoctorAppointments(Int32Value request, StreamObserver<Empty> responseObserver) {
        int id = request.getValue();
        this.logger.info("Cascade deleting all appointments for doctor with id: " + id);

        try {
            this.appointmentsRepository.genericCascadeDelete(id, "doctor");
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (RuntimeException e) {
            responseObserver.onError(ExceptionsMapper.toStatusRuntimeException(e));
        }
    }

    static Appointments.Appointment grpcFromDomainModel(Appointment appointment) {
        LocalDateTime dateTime = appointment.getDateTime();

        String date = dateTime.format(dateFormatter);
        String time = dateTime.format(timeFormatter);

        return Appointments.Appointment.newBuilder()
            .setId(appointment.getId())
            .setDate(date)
            .setTime(time)
            .setStatus(appointment.getStatus())
            .setDoctorId(appointment.getDoctorId())
            .setPatientId(appointment.getPatientId())
            .build();
    }
}
