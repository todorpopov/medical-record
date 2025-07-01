package com.medrec.controllers;

import com.medrec.annotations.AuthGuard;
import com.medrec.utils.HTTPResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pages")
public class PageAuthController {

    @RequestMapping("/login")
    public ResponseEntity<HTTPResponse> login() {
        HTTPResponse response = new HTTPResponse(
            "SUCCESS",
            "Login page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/register")
    public ResponseEntity<HTTPResponse> register() {
        HTTPResponse response = new HTTPResponse(
            "SUCCESS",
            "Register page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }

    @AuthGuard({"admin"})
    @RequestMapping("/dashboard")
    public ResponseEntity<HTTPResponse> adminDashboard() {
        HTTPResponse response = new HTTPResponse(
            "SUCCESS",
            "Dashboard page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }

    @AuthGuard({"doctor", "patient"})
    @RequestMapping("/menu")
    public ResponseEntity<HTTPResponse> doctorDashboard() {
        HTTPResponse response = new HTTPResponse(
            "SUCCESS",
            "Menu page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/home")
    public ResponseEntity<HTTPResponse> home() {
        HTTPResponse response = new HTTPResponse(
            "SUCCESS",
            "Home page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }

    @AuthGuard({"admin"})
    @RequestMapping("/admin-query")
    public ResponseEntity<HTTPResponse> adminQuery() {
        HTTPResponse response = new HTTPResponse(
            "SUCCESS",
            "Admin Query page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }
}
