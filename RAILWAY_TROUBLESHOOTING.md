# Railway Deployment Troubleshooting Guide

## 🔍 Common Issues and Solutions

### 1. Backend Service Returns 404 Errors

**Symptoms:**
- Frontend can't connect to backend
- CORS preflight requests fail with 404
- Health check endpoint unreachable

**Solutions:**

#### A. Check Service Status
1. Go to Railway Dashboard → Your Project → Backend Service
2. Check "Deployments" tab for errors
3. View "Logs" for startup issues
4. Verify service is showing as "Active"

#### B. Verify Environment Variables
```bash
# Check if all required variables are set:
DATABASE_URL=${{Postgres.DATABASE_URL}}
SPRING_KAFKA_BOOTSTRAP_SERVERS=${{Kafka.RAILWAY_PRIVATE_DOMAIN}}:9092
FRONTEND_URL=https://${{Frontend.RAILWAY_PUBLIC_DOMAIN}}
GOOGLE_CLIENT_ID=your-actual-client-id
GOOGLE_CLIENT_SECRET=your-actual-client-secret
```

#### C. Check PORT Configuration
- Backend must listen on Railway's PORT environment variable
- Verify Dockerfile includes: `ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar /app/app.jar"]`
- Check application.properties has: `server.port=${PORT:8080}`

#### D. Database Connection Issues
- Ensure PostgreSQL service is active
- Check DATABASE_URL format: `postgres://user:pass@host:port/db`
- Verify DatabaseConfig.java handles Railway URL conversion

### 2. Frontend Can't Reach Backend

**Symptoms:**
- Login fails with network errors
- API calls timeout
- CORS errors in browser console

**Solutions:**

#### A. Check Frontend Environment Variables
```bash
VITE_API_BASE_URL=https://${{Backend.RAILWAY_PUBLIC_DOMAIN}}
```

#### B. Verify Backend URL Detection
- Check constants.ts API_BASE_URL logic
- Ensure Railway domain pattern matching works
- Test with explicit VITE_API_BASE_URL if dynamic detection fails

#### C. CORS Configuration
- Verify SecurityConfig includes Railway domains
- Check `https://*.up.railway.app` pattern is allowed
- Ensure OPTIONS method is permitted for preflight requests

### 3. Kafka Connection Issues

**Symptoms:**
- Messages not being processed
- Kafka consumer errors in logs
- Backend startup fails

**Solutions:**

#### A. Check Kafka Service
- Verify Kafka service is running
- Check Kafka environment variables:
  ```bash
  KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://${{RAILWAY_PRIVATE_DOMAIN}}:9092
  ```

#### B. Backend Kafka Configuration
- Verify SPRING_KAFKA_BOOTSTRAP_SERVERS points to Kafka service
- Check KafkaConfig classes use environment variables
- Make Kafka optional for startup if needed

### 4. Build and Deployment Failures

**Symptoms:**
- Build fails during deployment
- Service won't start after deployment
- Missing dependencies

**Solutions:**

#### A. Docker Build Issues
- Check Dockerfile syntax and paths
- Verify all required files are included
- Test Docker build locally first

#### B. Java/Maven Issues
- Ensure Java 21 compatibility
- Check pom.xml dependencies
- Verify Maven wrapper permissions

#### C. Node.js/npm Issues
- Check package.json dependencies
- Verify Node.js version compatibility
- Clear npm cache if needed

### 5. Authentication Issues

**Symptoms:**
- Google OAuth fails
- JWT token validation errors
- 401/403 errors

**Solutions:**

#### A. Google OAuth Configuration
- Update Google Cloud Console with Railway URLs
- Verify CLIENT_ID and CLIENT_SECRET are correct
- Check redirect URI configuration

#### B. JWT Token Issues
- Verify GoogleJwtService is properly configured
- Check token extraction and validation logic
- Ensure proper CORS handling for auth endpoints

## 🛠️ Debugging Commands

### Check Service Health
```bash
# Test backend health endpoint
curl https://your-backend-url.up.railway.app/api/health

# Test environment variables
curl https://your-backend-url.up.railway.app/api/env-check
```

### View Railway Logs
1. Railway Dashboard → Service → Logs tab
2. Look for startup errors, connection issues
3. Check for port binding and health check failures

### Local Testing
```bash
# Test Docker build locally
docker build -t woochat-backend ./backend
docker run -p 8080:8080 woochat-backend

# Test with Railway-like environment
PORT=3000 docker run -p 3000:3000 woochat-backend
```

## 📋 Deployment Checklist

- [ ] All services created in correct order (Postgres → Kafka → Backend → Frontend)
- [ ] Root directories set correctly for each service
- [ ] Environment variables configured
- [ ] Google OAuth URLs updated
- [ ] Health checks passing
- [ ] CORS configured for Railway domains
- [ ] Database connection working
- [ ] Kafka connection optional/working

## 🚨 Emergency Fixes

### Quick Health Check Fix
If health endpoint is failing, verify:
1. `/api/health` endpoint is accessible without auth
2. SecurityConfig permits health endpoints
3. HealthController is properly mapped

### Quick CORS Fix
If CORS is blocking requests:
1. Add explicit Railway domain to SecurityConfig
2. Ensure OPTIONS method is allowed
3. Check preflight request handling

### Quick Database Fix
If database connection fails:
1. Verify DATABASE_URL environment variable
2. Check DatabaseConfig.java URL parsing
3. Ensure PostgreSQL service is active

## 💡 Performance Tips

1. **Use Railway Sleep**: Turn off services when not in use
2. **Monitor Resources**: Check CPU/memory usage in dashboard
3. **Optimize Docker**: Use multi-stage builds and smaller base images
4. **Database Optimization**: Use connection pooling and proper indexing

## 🔗 Useful Links

- [Railway Documentation](https://docs.railway.app/)
- [Railway Environment Variables](https://docs.railway.app/develop/variables)
- [Railway Networking](https://docs.railway.app/develop/networking)
- [Railway Logs](https://docs.railway.app/develop/logs)