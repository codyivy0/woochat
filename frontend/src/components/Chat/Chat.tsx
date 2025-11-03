import React, { useState, useEffect, useRef } from "react";
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  Avatar,
  Box,
  Container,
  Paper,
  Chip,
  TextField,
  List,
  ListItem,
  ListItemAvatar,
  ListItemText,
  IconButton,
  CircularProgress,
} from "@mui/material";
import {
  Logout as LogoutIcon,
  Chat as ChatIcon,
  Security as SecurityIcon,
  Token as TokenIcon,
  Send as SendIcon,
} from "@mui/icons-material";

interface Message {
  id: number;
  content: string;
  createdAt: string;
  sender: {
    id: string;
    name: string;
    picture: string;
  };
}

interface ChatPageProps {
  tokens: any;
  user: any;
  onLogout: () => void;
}

const ChatPage: React.FC<ChatPageProps> = ({ tokens, user, onLogout }) => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [newMessage, setNewMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const [fetchingMessages, setFetchingMessages] = useState(true);
  const messagesEndRef = useRef<HTMLDivElement>(null);

  // Scroll to bottom when new messages arrive
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // Fetch messages on component mount and poll for updates
  useEffect(() => {
    fetchMessages();

    // Poll for new messages every 3 seconds
    const interval = setInterval(fetchMessages, 3000);
    return () => clearInterval(interval);
  }, []);

  const fetchMessages = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/chat/messages");

      if (response.ok) {
        const data = await response.json();
        setMessages(data.reverse()); // Reverse to show oldest first
      }
    } catch (error) {
      console.error("Error fetching messages:", error);
    } finally {
      setFetchingMessages(false);
    }
  };

  const sendMessage = async () => {
    if (!newMessage.trim() || loading) return;

    setLoading(true);
    try {
      const response = await fetch("http://localhost:8080/api/chat/messages", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          content: newMessage.trim(),
          userId: user.id,
        }),
      });

      if (response.ok) {
        setNewMessage("");
        fetchMessages(); // Refresh messages
      }
    } catch (error) {
      console.error("Error sending message:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyPress = (event: React.KeyboardEvent) => {
    if (event.key === "Enter" && !event.shiftKey) {
      event.preventDefault();
      sendMessage();
    }
  };

  const formatTime = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
  };

  return (
    <Box
      sx={{
        flexGrow: 1,
        height: "100vh",
        display: "flex",
        flexDirection: "column",
      }}
    >
      {/* App Bar */}
      <AppBar position="static">
        <Toolbar>
          <ChatIcon sx={{ mr: 2 }} />
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Chat App
          </Typography>

          {user && (
            <Box display="flex" alignItems="center" gap={2}>
              <Avatar src={user.picture} alt={user.name} />
              <Typography variant="body1">{user.name}</Typography>
              <Button
                color="inherit"
                onClick={onLogout}
                startIcon={<LogoutIcon />}
              >
                Logout
              </Button>
            </Box>
          )}
        </Toolbar>
      </AppBar>

      <Container
        maxWidth="lg"
        sx={{ mt: 2, mb: 2, flex: 1, display: "flex", flexDirection: "column" }}
      >
        {/* Dev Info Cards - Make them smaller */}
        <Box
          display="flex"
          gap={2}
          mb={2}
          flexDirection={{ xs: "column", md: "row" }}
        >
          <Box flex={1}>
            <Paper sx={{ p: 2 }}>
              <Box display="flex" alignItems="center" gap={1}>
                <SecurityIcon color="primary" />
                <Typography variant="body2">
                  <strong>{user?.name}</strong> ({user?.email})
                </Typography>
              </Box>
            </Paper>
          </Box>

          <Box flex={1}>
            <Paper sx={{ p: 2 }}>
              <Box display="flex" alignItems="center" gap={1}>
                <TokenIcon color="primary" />
                <Typography variant="body2">
                  Token expires in {tokens?.expires_in} seconds
                </Typography>
              </Box>
            </Paper>
          </Box>
        </Box>

        {/* Chat Area */}
        <Paper
          sx={{
            flex: 1,
            display: "flex",
            flexDirection: "column",
            overflow: "hidden",
          }}
        >
          {/* Messages Area */}
          <Box sx={{ flex: 1, overflow: "auto", p: 2 }}>
            {(() => {
              if (fetchingMessages) {
                return (
                  <Box
                    display="flex"
                    justifyContent="center"
                    alignItems="center"
                    height="100%"
                  >
                    <CircularProgress />
                  </Box>
                );
              }

              if (messages.length === 0) {
                return (
                  <Box
                    display="flex"
                    justifyContent="center"
                    alignItems="center"
                    height="100%"
                    flexDirection="column"
                  >
                    <ChatIcon
                      sx={{ fontSize: 60, color: "text.secondary", mb: 2 }}
                    />
                    <Typography variant="h6" color="textSecondary">
                      No messages yet
                    </Typography>
                    <Typography variant="body2" color="textSecondary">
                      Be the first to send a message!
                    </Typography>
                  </Box>
                );
              }

              return (
                <List sx={{ pt: 0 }}>
                  {messages.map((message) => (
                    <ListItem
                      key={message.id}
                      alignItems="flex-start"
                      sx={{ pb: 2 }}
                    >
                      <ListItemAvatar>
                        <Avatar
                          src={message.sender.picture}
                          alt={message.sender.name}
                        />
                      </ListItemAvatar>
                      <ListItemText
                        primary={
                          <Box display="flex" alignItems="center" gap={1}>
                            <Typography variant="subtitle2">
                              {message.sender.name}
                            </Typography>
                            <Typography variant="caption" color="textSecondary">
                              {formatTime(message.createdAt)}
                            </Typography>
                          </Box>
                        }
                        secondary={
                          <Typography variant="body1" sx={{ mt: 0.5 }}>
                            {message.content}
                          </Typography>
                        }
                      />
                    </ListItem>
                  ))}
                  <div ref={messagesEndRef} />
                </List>
              );
            })()}
          </Box>

          {/* Message Input */}
          <Box sx={{ p: 2, borderTop: 1, borderColor: "divider" }}>
            <Box display="flex" gap={1}>
              <TextField
                fullWidth
                variant="outlined"
                placeholder="Type a message..."
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                onKeyUp={handleKeyPress}
                disabled={loading}
                multiline
                maxRows={3}
              />
              <IconButton
                color="primary"
                onClick={sendMessage}
                disabled={loading || !newMessage.trim()}
                sx={{ alignSelf: "flex-end" }}
              >
                {loading ? <CircularProgress size={24} /> : <SendIcon />}
              </IconButton>
            </Box>
          </Box>
        </Paper>
      </Container>
    </Box>
  );
};

export default ChatPage;
