// Application constants
export const APP_CONFIG = {
  // Google OAuth
  GOOGLE_CLIENT_ID: "978081770993-kb0e6dgcom98td498ldgnnribpnd9eh7.apps.googleusercontent.com",
  
  // API Configuration
  // Environment-based API URL detection
  API_BASE_URL: (() => {
    // If VITE_API_BASE_URL is explicitly set, use it
    if (import.meta.env.VITE_API_BASE_URL) {
      return import.meta.env.VITE_API_BASE_URL;
    }
    
    // In production (Railway), try to detect backend URL pattern
    if (import.meta.env.PROD && globalThis.window !== undefined) {
      const currentHost = globalThis.window.location.host;
      // If we're on Railway (frontend-production-xxx.up.railway.app)
      if (currentHost.includes('frontend-production-') && currentHost.includes('.up.railway.app')) {
        // Try to construct backend URL by replacing 'frontend' with 'backend'
        const backendHost = currentHost.replace('frontend-production-', 'backend-production-');
        return `https://${backendHost}`;
      }
    }
    
    // Development fallback
    return "http://localhost:8080";
  })(),
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