import React from "react";
import { useGoogleLogin } from "@react-oauth/google";
import {
  Container,
  Paper,
  Typography,
  Button,
  Alert,
  Box,
  CircularProgress
} from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';

interface GoogleLoginProps {
  onLogin: (tokens: any) => void;
}

export default function GoogleLogin({ onLogin }: GoogleLoginProps) {
  const [isLoading, setIsLoading] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState<string | null>(null);

  const handleGoogleLogin = async (code: string) => {
    setIsLoading(true);
    try {
      const response = await fetch("http://localhost:8080/api/auth/google", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ code }),
      });
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setErrorMessage(null);

      if (onLogin) {
        onLogin(data);
      }

    } catch (error) {
      console.error("Google login failed", error);
      setErrorMessage("Login failed. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  const login = useGoogleLogin({
    flow: "auth-code",
    onSuccess: (codeResponse) => {
      setErrorMessage(null);
      console.log("Google login successful, code:", codeResponse.code);
      handleGoogleLogin(codeResponse.code);
    }, 
    onError: (errorResponse) => {
      console.error("Google login failed", errorResponse);
      setErrorMessage("Google login failed. Please try again.");
    },
    scope: "openid profile email",
  });

  return (
    <Container maxWidth="sm">
      <Box
        display="flex"
        flexDirection="column"
        alignItems="center"
        justifyContent="center"
        minHeight="100vh"
      >
        <Paper
          elevation={3}
          sx={{
            padding: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            width: '100%',
            maxWidth: 400
          }}
        >
          <Typography variant="h3" component="h1" gutterBottom color="primary">
            Chat App
          </Typography>
          
          <Typography variant="body1" color="textSecondary" sx={{ mb: 3, textAlign: 'center' }}>
            Sign in with your Google account to start chatting
          </Typography>

          {errorMessage && (
            <Alert severity="error" sx={{ width: '100%', mb: 2 }}>
              {errorMessage}
            </Alert>
          )}
          
          <Button
            variant="contained"
            size="large"
            onClick={() => login()}
            disabled={isLoading}
            startIcon={isLoading ? <CircularProgress size={20} /> : <GoogleIcon />}
            sx={{
              width: '100%',
              py: 1.5,
              backgroundColor: '#4285f4',
              '&:hover': {
                backgroundColor: '#3367d6',
              }
            }}
          >
            {isLoading ? "Signing in..." : "Sign in with Google"}
          </Button>
        </Paper>
      </Box>
    </Container>
  );
}