package com.woochat.controller;

import com.woochat.model.Message;
import com.woochat.model.User;
import com.woochat.repository.MessageRepository;
import com.woochat.repository.UserRepository;
import com.woochat.service.GoogleJwtService;
import com.woochat.service.KafkaMessageProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoogleJwtService googleJwtService;
    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages() {
        try {
            // Get messages ordered by creation time (oldest first)
            List<Message> messages = messageRepository.findAllByOrderByCreatedAtAsc();
            System.out.println("Found " + messages.size() + " messages");
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            System.err.println("Error fetching messages: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, String> request) {
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }
        
        String content = request.get("content");
        
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Get authenticated user from JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        
        String userId = authentication.getName();
        
        // Find user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            // Send message to Kafka instead of directly saving to database
            kafkaMessageProducer.sendChatMessage(content.trim(), user);
            
            // Return success immediately (asynchronous processing)
            return ResponseEntity.ok("Message sent successfully");
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send message");
        }
    }
}