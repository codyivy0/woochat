#!/bin/bash

echo "🚨 ENVSUBST TEMPLATE FIX"
echo "========================"
echo ""
echo "🔍 Issue: envsubst not processing \${PORT:-80} syntax correctly"
echo "✅ Fix: Use simple \${PORT} with explicit default in startup script"
echo ""
echo "Changes:"
echo "- Updated start.sh to set PORT=\${PORT:-80} explicitly"
echo "- Simplified nginx.conf.template to use \${PORT} only"
echo "- Added nginx -t configuration test"
echo "- Better error handling in startup script"
echo ""

# Add all changes
git add .

# Commit the envsubst fixes
git commit -m "🚨 ENVSUBST FIX: Fix PORT variable substitution

- Set explicit PORT=\${PORT:-80} in start.sh before envsubst
- Simplified nginx.conf.template to use \${PORT} only (not \${PORT:-80})
- Added nginx -t configuration test before starting
- Fixed envsubst template processing for Railway PORT

This should resolve the 'invalid port in \${PORT:-80}' nginx error."

echo "✅ Envsubst fix committed!"
echo ""
echo "🚀 DEPLOY IMMEDIATELY:"
echo "git push origin main"
echo ""
echo "🔍 How this works now:"
echo "1. start.sh sets PORT=\${PORT:-80} (bash default)"
echo "2. envsubst processes template: listen \${PORT}; → listen 80;"
echo "3. nginx gets valid config with actual port number"
echo "4. No more template syntax in final nginx config"