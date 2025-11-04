package com.woochat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private static final String NOT_SET = "NOT_SET";
    private static final String SET = "SET";
    private static final String DATABASE_URL_KEY = "DATABASE_URL";
    private static final String GOOGLE_CLIENT_ID_KEY = "GOOGLE_CLIENT_ID";
    private static final String GOOGLE_CLIENT_SECRET_KEY = "GOOGLE_CLIENT_SECRET";
    private static final String FRONTEND_URL_KEY = "FRONTEND_URL";
    private static final String KAFKA_BOOTSTRAP_SERVERS_KEY = "SPRING_KAFKA_BOOTSTRAP_SERVERS";

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "woochat-backend");
        response.put("timestamp", java.time.Instant.now().toString());
        response.put("port", System.getProperty("server.port", "8080"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> test() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Backend is working!");
        response.put("cors", "enabled");
        response.put("timestamp", java.time.Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/env-check")
    public ResponseEntity<Map<String, Object>> envCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", java.time.Instant.now().toString());
        
        // Check critical environment variables (without exposing secrets)
        Map<String, String> envStatus = new HashMap<>();
        envStatus.put(DATABASE_URL_KEY, System.getenv(DATABASE_URL_KEY) != null ? SET : NOT_SET);
        envStatus.put(GOOGLE_CLIENT_ID_KEY, System.getenv(GOOGLE_CLIENT_ID_KEY) != null ? SET : NOT_SET);
        envStatus.put(GOOGLE_CLIENT_SECRET_KEY, System.getenv(GOOGLE_CLIENT_SECRET_KEY) != null ? SET : NOT_SET);
        envStatus.put(FRONTEND_URL_KEY, System.getenv(FRONTEND_URL_KEY) != null ? System.getenv(FRONTEND_URL_KEY) : NOT_SET);
        envStatus.put(KAFKA_BOOTSTRAP_SERVERS_KEY, System.getenv(KAFKA_BOOTSTRAP_SERVERS_KEY) != null ? SET : NOT_SET);
        
        response.put("environment_variables", envStatus);
        response.put("profile", System.getProperty("spring.profiles.active", "default"));
        
        return ResponseEntity.ok(response);
    }
}