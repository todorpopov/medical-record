package com.medrec.interceptors;

import com.medrec.annotations.AuthGuard;
import com.medrec.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.List;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        AuthGuard authGuard = handlerMethod.getMethodAnnotation(AuthGuard.class);
        if (authGuard == null) {
            return true;
        }

        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Authentication required");
            return false;
        }

        List<String> requiredRoles = Arrays.stream(authGuard.value()).toList();
        boolean isAuthorized = authService.isRequestAuthorized(token, requiredRoles);
        if (!isAuthorized) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Insufficient privileges");
            return false;
        }

        return true;
    }
}
