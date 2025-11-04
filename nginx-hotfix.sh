#!/bin/bash

echo "🚨 NGINX WORKER PROCESS HOTFIX"
echo "==============================="
echo ""
echo "🔍 Issue: Too many nginx worker processes spawning"
echo "✅ Fix: Simplified nginx configuration with controlled worker processes"
echo ""
echo "Changes made:"
echo "- Set worker_processes to 1 (instead of auto)"
echo "- Simplified nginx.conf server block"
echo "- Added explicit nginx.main.conf with process control"
echo "- Updated Dockerfile to use both config files"
echo "- Fixed health check endpoint"
echo ""

# Add all changes
git add .

# Commit the nginx fixes
git commit -m "🚨 NGINX HOTFIX: Fix excessive worker processes

- Added nginx.main.conf with worker_processes=1
- Simplified nginx.conf server configuration
- Updated Dockerfile to use both config files
- Fixed health check to use /health endpoint
- Reduced worker connections to prevent resource issues

This should fix the excessive nginx worker processes error
and make the frontend start properly on Railway."

echo "✅ Nginx fixes committed!"
echo ""
echo "🚀 DEPLOY NOW:"
echo "git push origin main"
echo ""
echo "🔍 What this fixes:"
echo "- Controls nginx worker processes (set to 1)"
echo "- Proper health check endpoint at /health"
echo "- Simplified configuration for Railway"
echo "- Prevents resource exhaustion from too many workers"