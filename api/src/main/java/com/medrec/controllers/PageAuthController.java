package com.medrec.controllers;

import com.medrec.annotations.AuthGuard;
import com.medrec.utils.SuccessHTTPResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pages")
public class PageAuthController {

    @RequestMapping("/login")
    public ResponseEntity<SuccessHTTPResponse> login() {
        SuccessHTTPResponse response = new SuccessHTTPResponse(
            "SUCCESS",
            "Login page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }

    @RequestMapping("/register")
    public ResponseEntity<SuccessHTTPResponse> register() {
        SuccessHTTPResponse response = new SuccessHTTPResponse(
            "SUCCESS",
            "Register page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }

    @AuthGuard({"admin"})
    @RequestMapping("/dashboard")
    public ResponseEntity<SuccessHTTPResponse> adminDashboard() {
        SuccessHTTPResponse response = new SuccessHTTPResponse(
            "SUCCESS",
            "Dashboard page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }

    @AuthGuard({"doctor", "patient", "admin"})
    @RequestMapping("/menu")
    public ResponseEntity<SuccessHTTPResponse> doctorDashboard() {
        SuccessHTTPResponse response = new SuccessHTTPResponse(
            "SUCCESS",
            "Menu page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }

    @AuthGuard({})
    @RequestMapping("/home")
    public ResponseEntity<SuccessHTTPResponse> home() {
        SuccessHTTPResponse response = new SuccessHTTPResponse(
            "SUCCESS",
            "Home page is accessible to user"
        );
        return ResponseEntity.ok(response);
    }
}
