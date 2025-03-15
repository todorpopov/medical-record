package com.medrec.controllers;

import com.medrec.grpc.Users;
import com.medrec.repositories.UsersRepository;
import com.medrec.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UsersController {
    private final UsersRepository usersRepository;

    @Autowired
    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    @PostMapping("create")
    private String createDoctor(@RequestBody Map<String, String> body) {
        Users.Doctor doctor = Users.Doctor.newBuilder()
                .setFirstName(body.get("firstName"))
                .setLastName(body.get("lastName"))
                .setEmail(body.get("email"))
                .setPassword(body.get("password"))
                .setIsGp(true)
                .setSpecialtyId(1)
                .build();

        ResponseMessage response = this.usersRepository.createDoctor(doctor);
        return response.toString();
    }
}
