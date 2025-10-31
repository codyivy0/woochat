import React from 'react';
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
} from '@mui/material';
import {
  Logout as LogoutIcon,
  Chat as ChatIcon,
  Security as SecurityIcon,
  Token as TokenIcon
} from '@mui/icons-material';

interface ChatPageProps {
  tokens: any;
  user: any;
  onLogout: () => void;
}

const ChatPage: React.FC<ChatPageProps> = ({ tokens, user, onLogout }) => {
  return (
    <Box sx={{ flexGrow: 1 }}>
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
              <Typography variant="body1">
                {user.name}
              </Typography>
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

      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        {/* Cards Row */}
        <Box display="flex" gap={3} mb={3} flexDirection={{ xs: 'column', md: 'row' }}>
          {/* Authentication Status Card */}
          <Box flex={1}>
            <Paper sx={{ p: 3, height: '100%' }}>
              <Box display="flex" alignItems="center" mb={2}>
                <SecurityIcon color="primary" sx={{ mr: 1 }} />
                <Typography variant="h6">Authentication Status</Typography>
              </Box>
              
              <Box display="flex" flexDirection="column" gap={2}>
                <Chip 
                  label="✅ Authenticated" 
                  color="success" 
                  variant="outlined"
                />
                
                <Typography variant="body2">
                  <strong>Email:</strong> {user?.email || 'Loading...'}
                </Typography>
                
                <Typography variant="body2">
                  <strong>Name:</strong> {user?.name || 'Loading...'}
                </Typography>
                
                <Typography variant="body2">
                  <strong>User ID:</strong> {user?.id || 'Loading...'}
                </Typography>
              </Box>
            </Paper>
          </Box>

          {/* Token Information Card */}
          <Box flex={1}>
            <Paper sx={{ p: 3, height: '100%' }}>
              <Box display="flex" alignItems="center" mb={2}>
                <TokenIcon color="primary" sx={{ mr: 1 }} />
                <Typography variant="h6">Token Information</Typography>
              </Box>
              
              <Box display="flex" flexDirection="column" gap={2}>
                <Typography variant="body2">
                  <strong>Type:</strong> {tokens?.token_type}
                </Typography>
                
                <Typography variant="body2">
                  <strong>Expires In:</strong> {tokens?.expires_in} seconds
                </Typography>
                
                <Typography variant="body2" sx={{ wordBreak: 'break-all' }}>
                  <strong>Access Token:</strong> {tokens?.access_token?.substring(0, 40)}...
                </Typography>
              </Box>
            </Paper>
          </Box>
        </Box>

        {/* Chat Area */}
        <Paper sx={{ p: 4, textAlign: 'center', minHeight: 400 }}>
          <ChatIcon sx={{ fontSize: 60, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h4" gutterBottom>
            Chat Area
          </Typography>
          <Typography variant="body1" color="textSecondary">
            Chat functionality coming soon...
          </Typography>
          <Typography variant="body2" color="textSecondary" sx={{ mt: 1 }}>
            Your tokens are stored and ready for API calls!
          </Typography>
        </Paper>
      </Container>
    </Box>
  );
};

export default ChatPage;