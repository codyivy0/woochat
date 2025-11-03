package com.woochat.controller;

import com.woochat.model.User;
import com.woochat.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        
        if (code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body("Authorization code is required");
        }

        try {
            // Exchange code for tokens with Google
            Map<String, Object> tokenResponse = exchangeCodeForTokens(code);
            
            // Get user info from Google
            Map<String, Object> userInfo = getUserInfoFromGoogle(
                (String) tokenResponse.get("access_token")
            );
            
            // Save or update user in database
            User user = saveOrUpdateUser(userInfo);
            
            // Create response
            Map<String, Object> response = new HashMap<>();
            response.put("tokens", tokenResponse);
            response.put("user", user);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Google auth error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Authentication failed: " + e.getMessage());
        }
    }

    private User saveOrUpdateUser(Map<String, Object> userInfo) {
        String id = (String) userInfo.get("id");
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String picture = (String) userInfo.get("picture");
        
        User user = userRepository.findById(id).orElse(new User());
        user.setId(id);
        user.setEmail(email);
        user.setName(name);
        user.setPicture(picture);
        
        return userRepository.save(user);
    }
    private Map<String, Object> exchangeCodeForTokens(String code) {
        RestTemplate restTemplate = new RestTemplate();
        
        // Use form data instead of JSON
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("client_id", "978081770993-kb0e6dgcom98td498ldgnnribpnd9eh7.apps.googleusercontent.com");
        tokenRequest.add("client_secret", "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");
        tokenRequest.add("code", code);
        tokenRequest.add("grant_type", "authorization_code");
        tokenRequest.add("redirect_uri", "http://localhost:5173");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(tokenRequest, headers);
        
        return restTemplate.postForObject(
            "https://oauth2.googleapis.com/token",
            entity,
            Map.class
        );
    }

    private Map<String, Object> getUserInfoFromGoogle(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            "https://www.googleapis.com/oauth2/v2/userinfo",
            HttpMethod.GET,
            entity,
            Map.class
        ).getBody();
    }
}