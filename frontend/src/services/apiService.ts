import { APP_CONFIG } from "../config/constants";

export interface AuthResponse {
  token: string;
  user: {
    id: string;
    name: string;
    email: string;
    picture: string;
  };
}

export interface Message {
  id: number;
  content: string;
  createdAt: string;
  sender: {
    id: string;
    name: string;
    picture: string;
  };
}

class ApiService {
  private readonly baseUrl = APP_CONFIG.API_BASE_URL;

  private async makeRequest<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${this.baseUrl}${endpoint}`;
    const response = await fetch(url, {
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
      ...options,
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    return response.json();
  }

  // Authentication
  async authenticateWithGoogle(code: string): Promise<AuthResponse> {
    return this.makeRequest<AuthResponse>(APP_CONFIG.API_ENDPOINTS.AUTH_GOOGLE, {
      method: 'POST',
      body: JSON.stringify({ code }),
    });
  }

  // Messages
  async getMessages(token: string): Promise<Message[]> {
    return this.makeRequest<Message[]>(APP_CONFIG.API_ENDPOINTS.MESSAGES, {
      headers: {
        'Authorization': `Bearer ${token}`,
      },
    });
  }

  async sendMessage(token: string, content: string): Promise<Message> {
    return this.makeRequest<Message>(APP_CONFIG.API_ENDPOINTS.MESSAGES, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify({ content }),
    });
  }
}

export const apiService = new ApiService();