import { useState } from "react";
import GoogleLogin from "./components/GoogleLogin/GoogleLogin";
import { GoogleOAuthProvider } from "@react-oauth/google";
import "./App.css";

function App() {
  const [tokens, setTokens] = useState<string | null>(null);

  const CLIENTID = "978081770993-kb0e6dgcom98td498ldgnnribpnd9eh7.apps.googleusercontent.com";

  const handleLogin = (token: string) => {
    console.log("Received token:", token);
    setTokens(token);
  };

  if (tokens) {
    return (
      <div style={{ textAlign: "center", marginTop: "100px" }}>
        <h1>Welcome to the Chat App!</h1>
        <p>Your tokens: {tokens}</p>
      </div>
    );
  }

  return (
    <GoogleOAuthProvider clientId={CLIENTID}>
      <GoogleLogin onLogin={handleLogin} />
    </GoogleOAuthProvider>
  );
}

export default App;
