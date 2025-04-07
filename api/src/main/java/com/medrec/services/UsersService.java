package com.medrec.services;

import com.medrec.dtos.DoctorDTO;
import com.medrec.dtos.DoctorSummaryDTO;
import com.medrec.dtos.SpecialtyDTO;
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

    public List<SpecialtyDTO> getAllSpecialties() {
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

    public List<DoctorSummaryDTO> getAllGpDoctors() {
        this.logger.info("Retrieving all GP Doctors");

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
    }
}
