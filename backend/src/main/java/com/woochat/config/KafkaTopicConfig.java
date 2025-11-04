package com.woochat.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@ConditionalOnProperty(
    value = "spring.kafka.enabled", 
    havingValue = "true", 
    matchIfMissing = false
)
public class KafkaTopicConfig {

    public static final String CHAT_MESSAGES_TOPIC = "chat-messages";

    @Bean
    public NewTopic chatMessagesTopic() {
        return TopicBuilder.name(CHAT_MESSAGES_TOPIC)
                .partitions(3)  // Multiple partitions for scalability
                .replicas(1)    // Single replica for development
                .build();
    }
}