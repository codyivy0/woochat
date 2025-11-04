#!/bin/bash

# Railway Deployment Setup Script
echo "🚂 Setting up WooChat for Railway Deployment..."

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check required tools
if ! command_exists git; then
    echo "❌ Git is required for Railway deployment"
    exit 1
fi

# Ensure we're in a git repository
if [ ! -d ".git" ]; then
    echo "📂 Initializing git repository..."
    git init
    git add .
    git commit -m "Initial commit for Railway deployment"
fi

echo ""
echo "📋 Railway Deployment Checklist:"
echo ""
echo "1. 🔑 Environment Variables to Set in Railway:"
echo "   Backend Service:"
echo "   - DATABASE_URL=\${{Postgres.DATABASE_URL}}"
echo "   - SPRING_KAFKA_BOOTSTRAP_SERVERS=\${{Kafka.RAILWAY_PRIVATE_DOMAIN}}:9092"
echo "   - FRONTEND_URL=https://\${{Frontend.RAILWAY_PUBLIC_DOMAIN}}"
echo "   - GOOGLE_CLIENT_ID=your-google-client-id"
echo "   - GOOGLE_CLIENT_SECRET=your-google-client-secret"
echo ""
echo "   Frontend Service:"
echo "   - VITE_API_BASE_URL=https://\${{Backend.RAILWAY_PUBLIC_DOMAIN}}"
echo ""
echo "   Kafka Service:"
echo "   - KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://\${{RAILWAY_PRIVATE_DOMAIN}}:9092"
echo ""
echo "2. 🏗️ Service Setup Order:"
echo "   1. Add PostgreSQL Database"
echo "   2. Add Kafka service (Root Directory: /kafka)"
echo "   3. Add Backend service (Root Directory: /backend)"
echo "   4. Add Frontend service (Root Directory: /frontend)"
echo ""
echo "3. 🔄 Deployment Commands:"
echo "   git add ."
echo "   git commit -m 'Deploy to Railway'"
echo "   git push origin main"
echo ""
echo "4. 🌐 Update Google OAuth:"
echo "   Add your Railway frontend URL to Google Cloud Console"
echo "   Format: https://frontend-production-xxxx.up.railway.app"
echo ""
echo "✅ Railway configuration files created:"
echo "   - railway.json (root)"
echo "   - backend/railway.json"
echo "   - frontend/railway.json"
echo "   - kafka/railway.json"
echo ""
echo "✅ Backend configured for Railway PORT environment variable"
echo "✅ Health endpoints configured for Railway monitoring"
echo "✅ CORS configured for Railway domains"
echo "✅ Database configuration supports Railway environment variables"
echo ""
echo "🎯 Next Steps:"
echo "1. Create a Railway project at https://railway.app"
echo "2. Connect your GitHub repository"
echo "3. Add services in the specified order"
echo "4. Set environment variables as listed above"
echo "5. Deploy with 'git push'"
echo ""
echo "💡 Pro Tip: Use Railway's sleep feature to save credits when not actively using the app!"