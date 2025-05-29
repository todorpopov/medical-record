package com.medrec.dtos.appointments.appointment;

import com.medrec.exception_handling.exceptions.BadRequestException;
import com.medrec.exception_handling.exceptions.IdNotSetException;
import com.medrec.grpc.appointments.Appointments;

public class UpdateAppointmentDTO {
    private Integer id;
    private String status;

    public UpdateAppointmentDTO() {
    }

    public UpdateAppointmentDTO(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Appointments.UpdateAppointmentRequest safeConvertToGrpcRequest() throws RuntimeException{
        Appointments.UpdateAppointmentRequest.Builder requestBuilder = Appointments.UpdateAppointmentRequest.newBuilder();

        if (this.id == null) {
            throw new IdNotSetException("Appointment id is not set. Cannot update appointment");
        }

        if (this.status == null) {
            throw new BadRequestException("Appointment status is not set. Cannot update appointment status");
        }

        requestBuilder.setId(this.id);
        requestBuilder.setStatus(this.status);

        return requestBuilder.build();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
