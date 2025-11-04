# 💬 WooChat

A real-time chat application built with **React**, **Spring Boot**, **Apache Kafka**, and **PostgreSQL**.

## 🚀 Quick Start (Docker)

**One command to rule them all:**

```bash
./start-demo.sh
```

Or manually:

```bash
docker-compose up --build
```

**Access the application:**
- 🌐 **Frontend**: http://localhost:3000
- 🔧 **Backend API**: http://localhost:8080
- 📊 **Health Check**: http://localhost:8080/api/health

## ☁️ Railway Deployment

**Deploy to Railway cloud platform:**

```bash
./deploy-railway.sh
```

This will show you the complete Railway deployment guide. See `RAILWAY_DEPLOY.md` for detailed instructions and `RAILWAY_TROUBLESHOOTING.md` for common issues and solutions.

## 🏗️ Architecture

```
┌─────────────┐    ┌──────────────┐    ┌─────────────┐    ┌──────────────┐
│   React     │    │ Spring Boot  │    │   Apache    │    │ PostgreSQL   │
│  Frontend   │◄──►│   Backend    │◄──►│   Kafka     │◄──►│  Database    │
│   (Nginx)   │    │   (REST)     │    │ (Messages)  │    │   (Data)     │
└─────────────┘    └──────────────┘    └─────────────┘    └──────────────┘
```

### **Message Flow:**
1. **User sends message** → Frontend → REST API
2. **Message published** → Kafka Topic (asynchronous)
3. **Kafka consumer** → Processes message → Saves to database
4. **Frontend polls** → Gets updated messages

## 🛠️ Technology Stack

### **Frontend**
- **React 18** with TypeScript
- **Material-UI** for components
- **Google OAuth** for authentication
- **Nginx** for production serving

### **Backend**
- **Spring Boot 3.5.7** with Java 21
- **Spring Security** with OAuth2 JWT
- **Spring Data JPA** with PostgreSQL
- **Apache Kafka** for async messaging

### **Infrastructure**
- **Docker & Docker Compose** for containerization
- **PostgreSQL 15** for data persistence
- **Apache Kafka** with KRaft mode (no Zookeeper)

## 📋 Prerequisites

- **Docker Desktop** (includes Docker Compose)
- **Git** (to clone the repository)

That's it! No need to install Node.js, Java, Maven, or PostgreSQL locally.

## 🔧 Development Setup

If you want to run services individually for development:

### **Start Infrastructure Only:**
```bash
# Start just database and Kafka
docker-compose up postgres kafka -d
```

### **Backend Development:**
```bash
cd backend
./mvnw spring-boot:run
```

### **Frontend Development:**
```bash
cd frontend
npm install
npm run dev
```

## 📊 Monitoring & Logs

### **Check Service Status:**
```bash
docker-compose ps
```

### **View Logs:**
```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f kafka
```

### **Health Checks:**
- **Backend**: http://localhost:8080/actuator/health
- **Frontend**: http://localhost:3000 (should load the app)

## 🔍 Kafka Monitoring

### **List Topics:**
```bash
docker exec chat-kafka kafka-topics --bootstrap-server localhost:9092 --list
```

### **Read Messages:**
```bash
docker exec chat-kafka kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic chat-messages \
  --from-beginning
```

## 🛑 Stopping the Application

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (clears database)
docker-compose down -v
```

## 🚀 Features

- ✅ **Real-time messaging** with Kafka
- ✅ **Google OAuth authentication**
- ✅ **Responsive Material-UI design**
- ✅ **Containerized deployment**
- ✅ **Async message processing**
- ✅ **Production-ready setup**

## 🔐 Authentication

Uses **Google OAuth 2.0** with JWT tokens. Simply click "Sign in with Google" to authenticate.

## 📝 API Endpoints

- `POST /api/auth/google` - Google OAuth authentication
- `GET /api/chat/messages` - Fetch chat messages
- `POST /api/chat/messages` - Send new message (requires auth)

## 🎯 Demo Notes

This is a demonstration application showcasing:
- **Microservices architecture** with Docker
- **Event-driven messaging** with Kafka
- **Modern React** with TypeScript
- **Spring Boot** best practices
- **OAuth2 security** implementation
