package com.medrec.services;

import com.medrec.dtos.users.doctor.DoctorDTO;
import com.medrec.dtos.users.doctor.DoctorSummaryDTO;
import com.medrec.dtos.users.doctor.RegisterDoctorDTO;
import com.medrec.dtos.users.doctor.UpdateDoctorDTO;
import com.medrec.dtos.users.patient.PatientDTO;
import com.medrec.dtos.users.patient.RegisterPatientDTO;
import com.medrec.dtos.users.specialty.SpecialtyDTO;
import com.medrec.gateways.UsersGateway;
import com.medrec.grpc.users.Users;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Component
public class UsersService {
    private final Logger logger = Logger.getLogger(UsersService.class.getName());

    private final UsersGateway usersGateway;

    public UsersService(UsersGateway usersGateway) {
        this.usersGateway = usersGateway;
    }

    public DoctorDTO createDoctor(RegisterDoctorDTO doctor) throws RuntimeException {
        this.logger.info("Creating new doctor");

        Users.CreateDoctorRequest request = Users.CreateDoctorRequest.newBuilder()
            .setFirstName(doctor.getFirstName())
            .setLastName(doctor.getLastName())
            .setEmail(doctor.getEmail())
            .setPassword(doctor.getPassword())
            .setSpecialtyId(doctor.getSpecialtyId())
            .setIsGp(doctor.isGeneralPractitioner())
            .build();

        try {
            Users.Doctor savedDoctor = this.usersGateway.createDoctor(request);

            SpecialtyDTO specialtyDto = new SpecialtyDTO(
                savedDoctor.getSpecialty().getId(),
                savedDoctor.getSpecialty().getSpecialtyName(),
                savedDoctor.getSpecialty().getSpecialtyDescription()
            );

            DoctorDTO doctorDto = new DoctorDTO(
                savedDoctor.getId(),
                savedDoctor.getFirstName(),
                savedDoctor.getLastName(),
                savedDoctor.getEmail(),
                savedDoctor.getPassword(),
                savedDoctor.getIsGp(),
                specialtyDto
            );

            this.logger.info("Created doctor: " + doctorDto.getId());
            return doctorDto;
        } catch (RuntimeException e) {
            this.logger.info("Error creating doctor: " + e.getMessage());
            throw e;
        }
    }

    public DoctorDTO getDoctorById(int id) throws RuntimeException {
        this.logger.info("Retrieving doctor with id: " + id);

        try {
            Users.Doctor doctor = this.usersGateway.getDoctorById(id);

            SpecialtyDTO specialtyDto = new SpecialtyDTO(
                doctor.getSpecialty().getId(),
                doctor.getSpecialty().getSpecialtyName(),
                doctor.getSpecialty().getSpecialtyDescription()
            );

            DoctorDTO doctorDto = new DoctorDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getEmail(),
                doctor.getPassword(),
                doctor.getIsGp(),
                specialtyDto
            );

            this.logger.info("Retrieved doctor: " + doctorDto.getId());
            return doctorDto;
        } catch (RuntimeException e) {
            this.logger.info("Error retrieving doctor: " + e.getMessage());
            throw e;
        }
    }

    public DoctorDTO getDoctorByEmail(String email) throws RuntimeException {
        this.logger.info("Retrieving doctor with email: " + email);

        try {
            Users.Doctor doctor = this.usersGateway.getDoctorByEmail(email);

            SpecialtyDTO specialtyDto = new SpecialtyDTO(
                doctor.getSpecialty().getId(),
                doctor.getSpecialty().getSpecialtyName(),
                doctor.getSpecialty().getSpecialtyDescription()
            );

            DoctorDTO doctorDto = new DoctorDTO(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getEmail(),
                doctor.getPassword(),
                doctor.getIsGp(),
                specialtyDto
            );

            this.logger.info("Retrieved doctor: " + doctorDto.getId());
            return doctorDto;
        } catch (RuntimeException e) {
            this.logger.info("Error retrieving doctor: " + e.getMessage());
            throw e;
        }
    }

    public List<DoctorDTO> getAllDoctors() throws RuntimeException {
        this.logger.info("Retrieving all doctors");

        try {
            Users.DoctorList doctors = this.usersGateway.getAllDoctors();
            List<DoctorDTO> doctorDtos = new ArrayList<>();

            doctors.getDoctorsList().forEach(d -> {
                SpecialtyDTO specialtyDto = new SpecialtyDTO(
                    d.getSpecialty().getId(),
                    d.getSpecialty().getSpecialtyName(),
                    d.getSpecialty().getSpecialtyDescription()
                );

                DoctorDTO dto = new DoctorDTO(
                    d.getId(),
                    d.getFirstName(),
                    d.getLastName(),
                    d.getEmail(),
                    d.getPassword(),
                    d.getIsGp(),
                    specialtyDto
                );

                doctorDtos.add(dto);
            });

            this.logger.info("Retrieved " + doctorDtos.size() + " doctors");
            return doctorDtos;
        } catch (RuntimeException e) {
            this.logger.info("Error retrieving doctors: " + e.getMessage());
            throw e;
        }
    }

    public List<DoctorSummaryDTO> getAllGpDoctors() {
        this.logger.info("Retrieving all GP Doctors");

        try {
            Users.DoctorList gpDoctors = usersGateway.getAllGpDoctors();
            List<DoctorSummaryDTO> doctorDtos = new ArrayList<>();

            gpDoctors.getDoctorsList().forEach(d -> {
                SpecialtyDTO specialtyDto = new SpecialtyDTO(
                    d.getSpecialty().getId(),
                    d.getSpecialty().getSpecialtyName(),
                    d.getSpecialty().getSpecialtyDescription()
                );

                DoctorSummaryDTO dto = new DoctorSummaryDTO(
                    d.getId(),
                    d.getFirstName(),
                    d.getLastName(),
                    d.getIsGp(),
                    specialtyDto
                );

                doctorDtos.add(dto);
            });

            this.logger.info("Retrieved " + doctorDtos.size() + " GP doctors");
            return doctorDtos;
        } catch (RuntimeException e) {
            this.logger.info("Error retrieving GP doctors: " + e.getMessage());
            throw e;
        }
    }

    public DoctorDTO updateDoctor(UpdateDoctorDTO doctor) throws RuntimeException {
        this.logger.info("Updating doctor with id: " + doctor.getId());

        try {
            Users.Doctor updatedDoctor = this.usersGateway.updateDoctor(doctor.safeConvertToGrpcRequest());

            SpecialtyDTO specialtyDto = new SpecialtyDTO(
                updatedDoctor.getSpecialty().getId(),
                updatedDoctor.getSpecialty().getSpecialtyName(),
                updatedDoctor.getSpecialty().getSpecialtyDescription()
            );

            DoctorDTO dto = new DoctorDTO(
                updatedDoctor.getId(),
                updatedDoctor.getFirstName(),
                updatedDoctor.getLastName(),
                updatedDoctor.getEmail(),
                updatedDoctor.getPassword(),
                updatedDoctor.getIsGp(),
                specialtyDto
            );

            this.logger.info("Updated doctor: " + dto.getId());
            return dto;
        } catch (RuntimeException e) {
            this.logger.info("Error updating doctor: " + e.getMessage());
            throw e;
        }
    }

    public void deleteDoctorById(int id) throws RuntimeException {
        this.logger.info("Deleting doctor with id: " + id);

        try {
            this.usersGateway.deleteDoctor(id);
            this.logger.info("Deleted doctor with id: " + id);
        } catch (RuntimeException e) {
            this.logger.info("Error deleting doctor: " + e.getMessage());
            throw e;
        }
    }

    public PatientDTO createPatient(RegisterPatientDTO patient) throws RuntimeException {
        this.logger.info("Creating new patient");

        try {
            Users.CreatePatientRequest request = Users.CreatePatientRequest.newBuilder()
                .setFirstName(patient.getFirstName())
                .setLastName(patient.getLastName())
                .setEmail(patient.getEmail())
                .setPassword(patient.getPassword())
                .setPin(patient.getPin())
                .setGpId(patient.getGpId())
                .setIsHealthInsured(patient.isInsured())
                .build();

            Users.Patient savedPatient = this.usersGateway.createPatientDoctorId()
        }
    }

    public List<SpecialtyDTO> getAllSpecialties() throws RuntimeException {
        this.logger.info("Retrieving all specialties");

        Users.SpecialtiesList specialties = usersGateway.getAllSpecialties();
        List<SpecialtyDTO> specialtyDtos = new ArrayList<>();
        specialties.getSpecialtiesList().forEach(specialty -> {
            SpecialtyDTO dto = new SpecialtyDTO(
                specialty.getId(),
                specialty.getSpecialtyName(),
                specialty.getSpecialtyDescription()
            );
            specialtyDtos.add(dto);
        });

        this.logger.info("Retrieved " + specialtyDtos.size() + " specialties");

        return specialtyDtos;
    }
}
