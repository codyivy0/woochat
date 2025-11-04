package com.woochat.service;

import com.woochat.config.KafkaTopicConfig;
import com.woochat.dto.ChatMessageDto;
import com.woochat.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

/**
 * Service responsible for sending chat messages to Kafka
 * This replaces direct database saves with asynchronous message publishing
 * Only active when Kafka is enabled
 */
@Service
@ConditionalOnProperty(
    value = "spring.kafka.enabled", 
    havingValue = "true", 
    matchIfMissing = false
)
public class KafkaMessageProducer {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageProducer.class);
    
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    /**
     * Sends a chat message to Kafka topic
     * @param content The message content
     * @param user The user who sent the message
     * @return CompletableFuture for asynchronous handling
     */
    public CompletableFuture<SendResult<String, Object>> sendChatMessage(String content, User user) {
        // Create DTO with all necessary information
        ChatMessageDto messageDto = new ChatMessageDto(
            content,
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPicture(),
            LocalDateTime.now()
        );
        
        // Use user ID as the message key for partitioning
        // This ensures all messages from the same user go to the same partition
        String messageKey = user.getId();
        
        logger.info("Sending message to Kafka - User: {}, Content: '{}'", 
                   user.getName(), content);
        
        // Send to Kafka topic asynchronously
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(
            KafkaTopicConfig.CHAT_MESSAGES_TOPIC, 
            messageKey, 
            messageDto
        );
        
        // Add success and failure callbacks
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                logger.info("Message sent successfully - Offset: {}, Partition: {}", 
                           result.getRecordMetadata().offset(), 
                           result.getRecordMetadata().partition());
            } else {
                logger.error("Failed to send message to Kafka", exception);
            }
        });
        
        return future;
    }
    
    /**
     * Sends a message synchronously (blocking)
     * Use this when you need to ensure the message is sent before continuing
     */
    public void sendChatMessageSync(String content, User user) {
        try {
            sendChatMessage(content, user).get(); // Block until completion
            logger.info("Message sent synchronously for user: {}", user.getName());
        } catch (Exception e) {
            logger.error("Failed to send message synchronously", e);
            throw new RuntimeException("Failed to send message to Kafka", e);
        }
    }
}