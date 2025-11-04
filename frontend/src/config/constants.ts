// Application constants
export const APP_CONFIG = {
  // Google OAuth
  GOOGLE_CLIENT_ID: "978081770993-kb0e6dgcom98td498ldgnnribpnd9eh7.apps.googleusercontent.com",
  
  // API Configuration
  // In Railway: Direct call to backend service URL
  // In Docker: API calls go through Nginx proxy to backend  
  // In development: Direct to localhost:8080
  API_BASE_URL: import.meta.env.VITE_API_BASE_URL || (import.meta.env.PROD ? "" : "http://localhost:8080"),
  API_ENDPOINTS: {
    AUTH_GOOGLE: "/api/auth/google",
    MESSAGES: "/api/chat/messages",
  },
  
  // UI Configuration
  MESSAGE_POLL_INTERVAL: 3000, // 3 seconds
  MAX_MESSAGE_LENGTH: 200,
  
  // Local Storage Keys
  STORAGE_KEYS: {
    JWT_TOKEN: "jwt_token",
    USER_DATA: "user_data",
  },
} as const;

// JWT Token validation
export const TOKEN_VALIDATION = {
  MIN_GOOGLE_TOKEN_LENGTH: 500, // Google ID tokens are typically much longer than custom JWTs
  JWT_PARTS_COUNT: 3,
} as const;