package com.medrec.controllers;

import com.medrec.grpc.Users;
import com.medrec.gateways.UsersGateway;
import com.medrec.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/users")
public class UsersController {
    private final UsersGateway usersGateway;

    @Autowired
    public UsersController(UsersGateway usersGateway) {
        this.usersGateway = usersGateway;
    }

    @CrossOrigin(origins = "http://localhost:4000")
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

        ResponseMessage response = this.usersGateway.createDoctor(doctor);
        return response.toString();
    }
}
