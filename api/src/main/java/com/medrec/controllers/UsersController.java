package com.medrec.controllers;

import com.medrec.grpc.users.Users;
import com.medrec.gateways.UsersGateway;
import com.medrec.services.UsersService;
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
}
