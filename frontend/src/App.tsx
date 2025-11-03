import { useState, useEffect } from "react";
import { GoogleOAuthProvider } from '@react-oauth/google';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
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

  const CLIENTID = "978081770993-kb0e6dgcom98td498ldgnnribpnd9eh7.apps.googleusercontent.com";

  const handleLogin = (data: {token: string, user: any}) => {
    console.log("Received auth data:", data);
    // Store JWT token and user data in localStorage for persistence
    localStorage.setItem('jwt_token', data.token);
    localStorage.setItem('user_data', JSON.stringify(data.user));
    setAuthData(data);
  };

  const handleLogout = () => {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('user_data');
    setAuthData(null);
  };

  // Check for existing token on app load
  useEffect(() => {
    const token = localStorage.getItem('jwt_token');
    const userData = localStorage.getItem('user_data');
    if (token && userData) {
      try {
        const user = JSON.parse(userData);
        setAuthData({ token, user });
      } catch (error) {
        console.error('Error parsing user data from localStorage:', error);
        // Clear invalid data
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('user_data');
      }
    }
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <GoogleOAuthProvider clientId={CLIENTID}>
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