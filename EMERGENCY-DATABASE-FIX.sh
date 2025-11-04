#!/bin/bash

echo "🚨 EMERGENCY DATABASE URL FIX"
echo "=============================="
echo ""
echo "🔍 CRITICAL ERROR FOUND:"
echo "Railway DATABASE_URL format: postgresql://..."
echo "Code was only checking for: postgres://..."
echo ""
echo "✅ EMERGENCY FIX:"
echo "- Updated DatabaseConfig to handle 'postgresql://' format"
echo "- Added support for both 'postgres://' and 'postgresql://'"
echo "- Enhanced logging to show Railway connection details"
echo ""
echo "This is the EXACT issue preventing backend startup!"
echo ""

# Compile to verify fix
echo "🔧 Testing compilation..."
cd backend && ./mvnw clean compile -DskipTests -q
if [ $? -eq 0 ]; then
    echo "✅ Compilation successful!"
else
    echo "❌ Compilation failed!"
    exit 1
fi
cd ..

# Add all changes
git add .

# Commit the critical fix
git commit -m "Fix Railway DATABASE_URL format support

CRITICAL FIX: Railway uses 'postgresql://' format, not 'postgres://'

Error was: Unsupported database URL format: postgresql://...
Fix: Updated DatabaseConfig.createDataSourceFromUrl() to handle both:
- postgres://... (original)
- postgresql://... (Railway format)

This was blocking all backend startup on Railway.
Backend should now connect to database successfully."

echo ""
echo "🚨 CRITICAL FIX COMMITTED!"
echo ""
echo "🚀 DEPLOY IMMEDIATELY - THIS IS THE FIX:"
echo "git push origin main"
echo ""
echo "📊 Expected result:"
echo "✅ Backend will now parse Railway DATABASE_URL correctly"
echo "✅ Database connection will succeed"
echo "✅ Health checks will pass"
echo "✅ Service will start successfully"
echo ""
echo "The error 'Unsupported database URL format' should be GONE!"