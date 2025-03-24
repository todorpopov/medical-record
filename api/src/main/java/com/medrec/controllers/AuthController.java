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

    @CrossOrigin(origins = "https://localhost:4000")
    @GetMapping("log-in")
    private String logIn(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String role = body.get("role");

        String token = null;

        switch (role) {
            case "admin":
                // Log admin in
            case "doctor":
                // Log doctor in
            case "patient":
                return this.authGateway.logPatientIn(email, password);
            default:
                return "Invalid role";
        }
    }
}
