package com.woochat.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for chat messages sent to Kafka
 * This represents the structure of messages in the Kafka topic
 */
public class ChatMessageDto {
    
    private String content;
    private String senderId;
    private String senderName;
    private String senderEmail;
    private String senderPicture;
    private LocalDateTime timestamp;
    
    // Default constructor (required for JSON deserialization)
    public ChatMessageDto() {}
    
    // Constructor for creating new messages
    public ChatMessageDto(String content, String senderId, String senderName, 
                         String senderEmail, String senderPicture, LocalDateTime timestamp) {
        this.content = content;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.senderPicture = senderPicture;
        this.timestamp = timestamp;
    }
    
    // Getters and Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    
    public String getSenderEmail() { return senderEmail; }
    public void setSenderEmail(String senderEmail) { this.senderEmail = senderEmail; }
    
    public String getSenderPicture() { return senderPicture; }
    public void setSenderPicture(String senderPicture) { this.senderPicture = senderPicture; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    
    @Override
    public String toString() {
        return "ChatMessageDto{" +
                "content='" + content + '\'' +
                ", senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}