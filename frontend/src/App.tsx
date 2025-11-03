import { useState, useEffect } from "react";
import { GoogleOAuthProvider } from '@react-oauth/google';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { APP_CONFIG, TOKEN_VALIDATION } from './config/constants';
import GoogleLogin from "./components/GoogleLogin/GoogleLogin";
import ChatPage from "./components/Chat/Chat";

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

function App() {
  const [authData, setAuthData] = useState<{token: string, user: any} | null>(null);

  const handleLogin = (data: {token: string, user: any}) => {
    console.log("Received auth data:", data);
    // Store JWT token and user data in localStorage for persistence
    localStorage.setItem(APP_CONFIG.STORAGE_KEYS.JWT_TOKEN, data.token);
    localStorage.setItem(APP_CONFIG.STORAGE_KEYS.USER_DATA, JSON.stringify(data.user));
    setAuthData(data);
  };

  const handleLogout = () => {
    localStorage.removeItem(APP_CONFIG.STORAGE_KEYS.JWT_TOKEN);
    localStorage.removeItem(APP_CONFIG.STORAGE_KEYS.USER_DATA);
    setAuthData(null);
  };

  // Helper function to clear invalid tokens
  const clearInvalidTokens = () => {
    console.log('Clearing invalid tokens, please log in again');
    localStorage.removeItem(APP_CONFIG.STORAGE_KEYS.JWT_TOKEN);
    localStorage.removeItem(APP_CONFIG.STORAGE_KEYS.USER_DATA);
    setAuthData(null);
  };

  // Check for existing token on app load
  useEffect(() => {
    const token = localStorage.getItem(APP_CONFIG.STORAGE_KEYS.JWT_TOKEN);
    const userData = localStorage.getItem(APP_CONFIG.STORAGE_KEYS.USER_DATA);
    
    // If we have both token and user data, try to use them
    if (token && userData) {
      try {
        const user = JSON.parse(userData);
        
        // Quick check: if token looks like our old custom JWT (3 parts), clear it
        if (token.split('.').length === TOKEN_VALIDATION.JWT_PARTS_COUNT && 
            token.length < TOKEN_VALIDATION.MIN_GOOGLE_TOKEN_LENGTH) {
          console.log('Found old custom JWT token, clearing...');
          clearInvalidTokens();
          return;
        }
        
        setAuthData({ token, user });
      } catch (error) {
        console.error('Error parsing user data from localStorage:', error);
        clearInvalidTokens();
      }
    }
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <GoogleOAuthProvider clientId={APP_CONFIG.GOOGLE_CLIENT_ID}>
        {authData ? (
          <ChatPage 
            token={authData.token} 
            user={authData.user} 
            onLogout={handleLogout} 
          />
        ) : (
          <GoogleLogin onLogin={handleLogin} />
        )}
      </GoogleOAuthProvider>
    </ThemeProvider>
  );
}

export default App;