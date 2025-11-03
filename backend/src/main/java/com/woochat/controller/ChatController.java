package com.woochat.controller;

import com.woochat.model.Message;
import com.woochat.model.User;
import com.woochat.repository.MessageRepository;
import com.woochat.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getMessages() {
        try {
            // Use findAll() first to test if basic query works
            List<Message> messages = messageRepository.findAll();
            System.out.println("Found " + messages.size() + " messages"); // Debug log
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            System.err.println("Error fetching messages: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> sendMessage(@RequestBody Map<String, String> request) {
        String content = request.get("content");
        String userId = request.get("userId");
        
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // Find user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Message message = new Message(content.trim(), user);
        Message savedMessage = messageRepository.save(message);
        
        return ResponseEntity.ok(savedMessage);
    }
}