# Railway Deployment Guide

## 🚂 Quick Railway Deployment

### Step 1: Create Railway Project
1. Go to [railway.app](https://railway.app)
2. Sign up/login with GitHub
3. "New Project" → "Empty Project"
4. Name it "WooChat"

### Step 2: Add Services (in this order)

#### A. PostgreSQL Database
- Click "Add Service" → "Database" → "PostgreSQL"
- Railway auto-configures everything

#### B. Kafka Service  
- Click "Add Service" → "GitHub Repo" → Select your woochat repo
- Set **Root Directory**: `/kafka`
- Railway auto-detects port 9092 from Dockerfile

#### C. Backend Service
- Click "Add Service" → "GitHub Repo" → Select your woochat repo  
- Set **Root Directory**: `/backend`
- Railway auto-detects port 8080 from Dockerfile

#### D. Frontend Service
- Click "Add Service" → "GitHub Repo" → Select your woochat repo
- Set **Root Directory**: `/frontend`
- Railway auto-detects port 80 from Dockerfile

### Step 3: Environment Variables

#### Backend Service Variables:
```
DATABASE_URL=${{Postgres.DATABASE_URL}}
PGUSER=${{Postgres.PGUSER}}
PGPASSWORD=${{Postgres.PGPASSWORD}}
SPRING_KAFKA_BOOTSTRAP_SERVERS=${{Kafka.RAILWAY_PRIVATE_DOMAIN}}:9092
FRONTEND_URL=${{Frontend.RAILWAY_PUBLIC_DOMAIN}}
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
SPRING_PROFILES_ACTIVE=production
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

#### Kafka Service Variables:
```
KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://${{RAILWAY_PRIVATE_DOMAIN}}:9092,EXTERNAL://0.0.0.0:9094
```

#### Frontend Service Variables:
```
VITE_API_BASE_URL=https://${{Backend.RAILWAY_PUBLIC_DOMAIN}}
```

### Step 4: Deploy
- Railway auto-deploys when you push to GitHub
- Or click "Deploy" in each service

### Step 5: Update Google OAuth
Add your frontend URL to Google Cloud Console:
```
https://your-frontend-xyz.up.railway.app
```

## 💰 Smart Usage Tips

### Turn Services On/Off:
- Each service has a "Sleep" button
- Click to pause when not using
- Saves your $5 credit!

### Monitor Usage:
- Dashboard shows hours used
- ~$5 = 100 hours of all services running
- Perfect for demos and testing

## 🔄 Development Workflow

1. **Develop locally** with `./start-demo.sh`
2. **Push to GitHub** when ready to show
3. **Railway auto-deploys**
4. **Sleep services** when done
5. **Wake up** for next demo

## ⚡ Quick Commands

### Deploy:
```bash
git add .
git commit -m "Update for Railway"
git push origin main
# Railway auto-deploys!
```

### Monitor:
- Check Railway dashboard for status
- View logs for debugging
- Monitor resource usage

Your app will be live at:
- Frontend: `https://frontend-xyz.up.railway.app`
- Backend: `https://backend-xyz.up.railway.app`