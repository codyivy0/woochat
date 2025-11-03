package com.woochat.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.woochat.config.AppConstants;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Map;

@Service
public class GoogleJwtService {
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Extract JWT claims - centralized parsing logic
    private JsonNode extractClaims(String googleIdToken) {
        try {
            String[] chunks = googleIdToken.split("\\.");
            if (chunks.length != 3) {
                throw new IllegalArgumentException("Invalid JWT format");
            }
            String payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
            return objectMapper.readTree(payload);
        } catch (Exception e) {
            System.err.println("Error extracting claims from Google token: " + e.getMessage());
            return null;
        }
    }

    public String extractUserIdFromGoogleToken(String googleIdToken) {
        JsonNode claims = extractClaims(googleIdToken);
        return claims != null && claims.has("sub") ? claims.get("sub").asText() : null;
    }

    public String extractEmailFromGoogleToken(String googleIdToken) {
        JsonNode claims = extractClaims(googleIdToken);
        return claims != null && claims.has("email") ? claims.get("email").asText() : null;
    }

    public boolean isValidGoogleToken(String googleIdToken) {
        try {
            // Verify token with Google
            String verifyUrl = AppConstants.GOOGLE_TOKEN_VERIFY_URL + googleIdToken;
            Map<String, Object> response = restTemplate.getForObject(verifyUrl, Map.class);
            
            if (response == null) {
                return false;
            }
            
            // Check if token is valid (Google returns error if invalid)
            return !response.containsKey("error");
            
        } catch (Exception e) {
            System.err.println("Error validating Google token: " + e.getMessage());
            return false;
        }
    }
}