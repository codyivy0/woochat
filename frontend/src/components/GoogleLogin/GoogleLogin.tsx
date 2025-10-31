import React from "react";
import { useGoogleLogin } from "@react-oauth/google";

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

      const { user, tokens } = data;
      if (onLogin) {
        onLogin(tokens);
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
    <div style={{ textAlign: "center", marginTop: "100px" }}>
      <h1>Chat App</h1>
      
      {errorMessage && (
        <div style={{ color: "red", marginBottom: "20px" }}>
          {errorMessage}
        </div>
      )}
      
      <button
        onClick={() => login()}
        disabled={isLoading}
        style={{
          padding: "12px 24px",
          fontSize: "16px",
          backgroundColor: "#4285f4",
          color: "white",
          border: "none",
          borderRadius: "4px",
          cursor: isLoading ? "not-allowed" : "pointer",
          opacity: isLoading ? 0.6 : 1,
        }}
      >
        {isLoading ? "Signing in..." : "Sign in with Google"}
      </button>
    </div>
  );
}