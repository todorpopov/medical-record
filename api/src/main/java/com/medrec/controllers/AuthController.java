package com.medrec.controllers;

import com.medrec.gateways.AuthGateway;
import com.medrec.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthGateway authGateway;

    @Autowired
    public AuthController(AuthGateway authGateway) {
        this.authGateway = authGateway;
    }
}
