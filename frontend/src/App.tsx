import { useState } from "react";
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
  const [authData, setAuthData] = useState<{tokens: any, user: any} | null>(null);

  const CLIENTID = "978081770993-kb0e6dgcom98td498ldgnnribpnd9eh7.apps.googleusercontent.com";

  const handleLogin = (data: {tokens: any, user: any}) => {
    console.log("Received auth data:", data);
    setAuthData(data);
  };

  const handleLogout = () => {
    setAuthData(null);
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <GoogleOAuthProvider clientId={CLIENTID}>
        {authData ? (
          <ChatPage 
            tokens={authData.tokens} 
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