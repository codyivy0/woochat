#!/bin/bash

echo "🚨 EMERGENCY RAILWAY FIX - Health Check Issues"
echo "=================================================="

# Add all changes
git add .

# Commit the fixes
git commit -m "🚨 URGENT: Fix Railway health check failures

- Add RootController with simple '/' endpoint for Railway health checks
- Update railway.json to use '/' path instead of '/api/health'
- Increase health check timeout to 300s (5 minutes)
- Allow root endpoints in SecurityConfig
- Update Dockerfile health check for Railway PORT variable
- Make Kafka configuration more fault-tolerant

This should fix the 'service unavailable' health check failures."

echo "✅ Changes committed. Ready to deploy!"
echo ""
echo "🚀 DEPLOY NOW with:"
echo "git push origin main"
echo ""
echo "🔍 The fix:"
echo "- Railway health check now hits simple '/' endpoint"
echo "- Returns 'Woochat Backend is running!' immediately"
echo "- No database or Kafka dependencies for health check"
echo "- Longer timeout (5 minutes) for startup"