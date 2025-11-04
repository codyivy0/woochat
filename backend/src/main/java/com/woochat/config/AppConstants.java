package com.woochat.config;

/**
 * Application-wide constants
 */
public final class AppConstants {
    
    // Google OAuth Configuration
    public static final String GOOGLE_CLIENT_ID = "978081770993-kb0e6dgcom98td498ldgnnribpnd9eh7.apps.googleusercontent.com";
    public static final String GOOGLE_CLIENT_SECRET = "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";
    public static final String GOOGLE_TOKEN_VERIFY_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
    public static final String GOOGLE_TOKEN_EXCHANGE_URL = "https://oauth2.googleapis.com/token";
    public static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    
    // Frontend Configuration
    public static final String FRONTEND_URL = "http://localhost:3000";
    
    // Kafka Configuration
    public static final String KAFKA_BOOTSTRAP_SERVERS = "kafka:9092";
    
    // Message Configuration
    public static final int MAX_MESSAGE_LENGTH = 200;
    public static final int MESSAGE_POLL_INTERVAL_MS = 3000;
    
    private AppConstants() {
        // Utility class - prevent instantiation
    }
}