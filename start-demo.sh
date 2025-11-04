#!/bin/bash

# WooChat Demo Startup Script
echo "🚀 Starting WooChat Demo Application..."
echo "This will spin up the complete chat application with:"
echo "  - React Frontend (http://localhost:3000)"
echo "  - Spring Boot Backend (http://localhost:8080)"
echo "  - PostgreSQL Database"
echo "  - Apache Kafka Message Broker"
echo ""

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker Desktop and try again."
    exit 1
fi

# Check if docker-compose is available
if ! command -v docker-compose > /dev/null 2>&1; then
    echo "❌ docker-compose is not installed. Please install Docker Compose and try again."
    exit 1
fi

echo "🔨 Building and starting all services..."
echo "This may take a few minutes on first run as Docker builds the images..."
echo ""

# Build and start all services
docker-compose up --build -d

# Wait a moment for services to start
echo "⏳ Waiting for services to start..."
sleep 10

# Check service status
echo ""
echo "📊 Service Status:"
docker-compose ps

echo ""
echo "🎉 WooChat Demo is starting up!"
echo ""
echo "📱 Frontend: http://localhost:3000"
echo "🔧 Backend API: http://localhost:8080"
echo "💾 Database: localhost:5432"
echo "📨 Kafka: localhost:9092"
echo ""
echo "To stop the application:"
echo "  docker-compose down"
echo ""
echo "To view logs:"
echo "  docker-compose logs -f [service-name]"
echo "  Examples:"
echo "    docker-compose logs -f frontend"
echo "    docker-compose logs -f backend"
echo "    docker-compose logs -f kafka"
echo ""
echo "🔍 Checking if services are healthy..."

# Function to check if service is responding
check_service() {
    local url=$1
    local name=$2
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -s "$url" > /dev/null 2>&1; then
            echo "✅ $name is ready!"
            return 0
        fi
        echo "⏳ Waiting for $name... (attempt $attempt/$max_attempts)"
        sleep 2
        ((attempt++))
    done
    
    echo "⚠️  $name may not be ready yet. Check logs with: docker-compose logs $name"
    return 1
}

# Check services
check_service "http://localhost:3000" "Frontend"
check_service "http://localhost:8080/actuator/health" "Backend"

echo ""
echo "🎊 All done! Your chat application should be running at:"
echo "🌐 http://localhost:3000"