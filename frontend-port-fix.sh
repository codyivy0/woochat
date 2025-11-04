#!/bin/bash

echo "🚨 FRONTEND PORT FIX FOR RAILWAY"
echo "================================="
echo ""
echo "🔍 Issue: Frontend not responding on Railway"
echo "✅ Fix: Added Railway PORT environment variable support"
echo ""
echo "Changes:"
echo "- Created nginx.conf.template with \${PORT} substitution"
echo "- Added start.sh script for environment variable processing"
echo "- Updated Dockerfile to use startup script"
echo "- Installed envsubst for template processing"
echo ""
echo "This allows nginx to listen on Railway's assigned PORT"
echo ""

# Add all changes
git add .

# Commit the frontend fixes
git commit -m "🚨 FRONTEND FIX: Add Railway PORT support

- Created nginx.conf.template with PORT variable substitution
- Added start.sh script to process environment variables
- Updated Dockerfile to use startup script with envsubst
- Frontend will now listen on Railway's assigned PORT
- Maintains health check endpoint functionality

This should fix frontend not responding on Railway."

echo "✅ Frontend PORT fix committed!"
echo ""
echo "🚀 DEPLOY NOW:"
echo "git push origin main"
echo ""
echo "🔍 What this fixes:"
echo "- Frontend listens on Railway's PORT (not hardcoded 80)"
echo "- Environment variable substitution in nginx config"
echo "- Proper startup sequence for Railway"
echo "- Health check endpoint remains functional"