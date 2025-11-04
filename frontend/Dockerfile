# Multi-stage build for React application
FROM node:18-alpine AS build

# Set working directory
WORKDIR /app

# Copy package files first (for better layer caching)
COPY package*.json ./

# Install ALL dependencies (including devDependencies for TypeScript build)
RUN npm ci

# Copy source code
COPY . .

# Build the React application
RUN npm run build

# Production stage with Nginx
FROM nginx:alpine

# Install envsubst and remove default nginx website
RUN apk add --no-cache gettext && \
    rm -rf /usr/share/nginx/html/*

# Copy built React app from build stage
COPY --from=build /app/dist /usr/share/nginx/html

# Copy custom nginx configurations
COPY nginx.main.conf /etc/nginx/nginx.conf
COPY nginx.conf.template /etc/nginx/conf.d/default.conf.template

# Copy startup script
COPY start.sh /start.sh
RUN chmod +x /start.sh

# Expose port 80 (Railway will map to its PORT)
EXPOSE 80

# Add health check with longer timeout for Railway
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:${PORT:-80}/health || exit 1

# Use startup script instead of direct nginx
CMD ["/start.sh"]