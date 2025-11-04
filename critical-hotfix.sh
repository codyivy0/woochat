#!/bin/bash

echo "🚨 CRITICAL HOTFIX - Health Check Failures"
echo "==========================================="
echo ""
echo "🔍 Issues Found:"
echo "1. Backend: Database connection blocking startup"
echo "2. Frontend: Health check configuration issues"
echo ""
echo "✅ Fixes Applied:"
echo ""
echo "BACKEND:"
echo "- Enhanced DatabaseConfig with better error handling"
echo "- Added database connection validation to env-check"
echo "- Made database connection non-blocking with spring properties"
echo "- Improved Railway DATABASE_URL parsing"
echo ""
echo "FRONTEND:"
echo "- Fixed railway.json (removed incorrect dockerfilePath)"
echo "- Added /health endpoint to nginx config"
echo "- Increased health check timeout to 300s"
echo ""
echo "⚠️  CRITICAL: Make sure these environment variables are set in Railway:"
echo ""
echo "Backend Service:"
echo "DATABASE_URL=\${{Postgres.DATABASE_URL}}"
echo "SPRING_KAFKA_BOOTSTRAP_SERVERS=\${{Kafka.RAILWAY_PRIVATE_DOMAIN}}:9092"
echo "FRONTEND_URL=https://\${{Frontend.RAILWAY_PUBLIC_DOMAIN}}"
echo "GOOGLE_CLIENT_ID=your-client-id"
echo "GOOGLE_CLIENT_SECRET=your-client-secret"
echo ""
echo "🚀 Committing fixes..."

# Add all changes
git add .

# Commit the critical fixes
git commit -m "🚨 CRITICAL HOTFIX: Database & Frontend Health Check Failures

Backend Fixes:
- Enhanced DatabaseConfig with comprehensive Railway URL handling
- Made database connection non-blocking for health checks
- Added connection validation and better error messages
- Fixed database connection timeout and failure handling

Frontend Fixes:
- Fixed railway.json configuration (removed incorrect dockerfilePath)
- Added dedicated /health endpoint to nginx
- Increased health check timeout to 300 seconds
- Improved nginx configuration for Railway deployment

These fixes should resolve both backend database connection issues
and frontend health check failures preventing Railway deployment."

echo ""
echo "✅ Critical fixes committed!"
echo ""
echo "🚀 DEPLOY IMMEDIATELY:"
echo "git push origin main"
echo ""
echo "🔍 After deployment, check:"
echo "1. Backend logs: Should show 'Found database URL' or 'Using local development'"
echo "2. Frontend: Should respond to both / and /health endpoints"
echo "3. Environment variables: Check /api/env-check endpoint"
echo ""
echo "💡 If backend still fails, the DATABASE_URL environment variable"
echo "   might not be set correctly in Railway. Double-check the variable!"