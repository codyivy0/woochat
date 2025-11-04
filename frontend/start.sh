#!/bin/sh

# Railway startup script for frontend
echo "🚀 Starting Frontend with PORT=${PORT:-80}"

# Set default PORT if not provided
export PORT=${PORT:-80}

# Substitute PORT environment variable in nginx template
envsubst '${PORT}' < /etc/nginx/conf.d/default.conf.template > /etc/nginx/conf.d/default.conf

# Show the generated config
echo "📄 Generated nginx config:"
cat /etc/nginx/conf.d/default.conf

# Test nginx configuration
echo "🔧 Testing nginx configuration..."
nginx -t

# Start nginx
echo "🌐 Starting nginx..."
nginx -g "daemon off;"