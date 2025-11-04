package com.woochat.service;

import com.woochat.config.KafkaTopicConfig;
import com.woochat.dto.ChatMessageDto;
import com.woochat.model.Message;
import com.woochat.model.User;
import com.woochat.repository.MessageRepository;
import com.woochat.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Service that consumes chat messages from Kafka and saves them to the database
 * This completes the asynchronous message processing pipeline
 * Only active when Kafka is enabled
 */
@Service
@ConditionalOnProperty(
    value = "spring.kafka.enabled", 
    havingValue = "true", 
    matchIfMissing = false
)
public class KafkaMessageConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageConsumer.class);
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Listens to the chat-messages Kafka topic and processes incoming messages
     * This method is automatically called when new messages arrive
     * 
     * @param messageDto The chat message from Kafka
     * @param partition The Kafka partition this message came from
     * @param offset The offset of this message in the partition
     * @param acknowledgment Manual acknowledgment to confirm processing
     */
    @KafkaListener(topics = KafkaTopicConfig.CHAT_MESSAGES_TOPIC, groupId = "chat-service-group")
    public void consumeChatMessage(
            @Payload ChatMessageDto messageDto,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            logger.info("Received message from Kafka - Partition: {}, Offset: {}, User: {}, Content: '{}'", 
                       partition, offset, messageDto.getSenderName(), messageDto.getContent());
            
            // Process the message
            boolean success = processMessage(messageDto);
            
            if (success) {
                // Acknowledge successful processing
                acknowledgment.acknowledge();
                logger.info("Message processed and acknowledged - Offset: {}", offset);
            } else {
                logger.error("Failed to process message - will not acknowledge. Offset: {}", offset);
                // Not acknowledging will cause Kafka to redeliver the message
            }
            
        } catch (Exception e) {
            logger.error("Error processing Kafka message at offset {}: {}", offset, e.getMessage(), e);
            // Don't acknowledge on error - message will be redelivered
        }
    }
    
    /**
     * Processes a single chat message by saving it to the database
     * 
     * @param messageDto The message to process
     * @return true if successful, false otherwise
     */
    private boolean processMessage(ChatMessageDto messageDto) {
        try {
            // Find or create the user
            User user = findOrCreateUser(messageDto);
            if (user == null) {
                logger.error("Failed to find or create user for message: {}", messageDto.getSenderId());
                return false;
            }
            
            // Create and save the message entity
            Message message = new Message(messageDto.getContent(), user);
            message.setCreatedAt(messageDto.getTimestamp());
            
            Message savedMessage = messageRepository.save(message);
            
            logger.info("Message saved to database - ID: {}, User: {}, Content: '{}'", 
                       savedMessage.getId(), user.getName(), message.getContent());
            
            return true;
            
        } catch (Exception e) {
            logger.error("Failed to save message to database: {}", e.getMessage(), e);
            return false;
        }
    }
    
    /**
     * Finds an existing user or creates a new one based on the message DTO
     * This handles cases where user data might have changed since the message was sent
     * 
     * @param messageDto The message containing user information
     * @return The user entity, or null if creation failed
     */
    private User findOrCreateUser(ChatMessageDto messageDto) {
        try {
            // Try to find existing user
            User existingUser = userRepository.findById(messageDto.getSenderId()).orElse(null);
            
            if (existingUser != null) {
                // Update user info if it has changed (user might have updated profile)
                boolean updated = false;
                
                if (!existingUser.getName().equals(messageDto.getSenderName())) {
                    existingUser.setName(messageDto.getSenderName());
                    updated = true;
                }
                
                if (!existingUser.getEmail().equals(messageDto.getSenderEmail())) {
                    existingUser.setEmail(messageDto.getSenderEmail());
                    updated = true;
                }
                
                if (!existingUser.getPicture().equals(messageDto.getSenderPicture())) {
                    existingUser.setPicture(messageDto.getSenderPicture());
                    updated = true;
                }
                
                if (updated) {
                    existingUser = userRepository.save(existingUser);
                    logger.info("Updated user information for: {}", existingUser.getName());
                }
                
                return existingUser;
            } else {
                // Create new user if not found
                User newUser = new User();
                newUser.setId(messageDto.getSenderId());
                newUser.setName(messageDto.getSenderName());
                newUser.setEmail(messageDto.getSenderEmail());
                newUser.setPicture(messageDto.getSenderPicture());
                
                User savedUser = userRepository.save(newUser);
                logger.info("Created new user: {}", savedUser.getName());
                
                return savedUser;
            }
            
        } catch (Exception e) {
            logger.error("Failed to find or create user {}: {}", messageDto.getSenderId(), e.getMessage(), e);
            return null;
        }
    }
}