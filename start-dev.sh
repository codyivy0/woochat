#!/bin/bash

# WooChat Development Startup Script
echo "🚀 Starting WooChat in Development Mode..."
echo "Frontend will run on http://localhost:3000 (same as Docker)"
echo "Backend will run on http://localhost:8080"
echo ""

# Check if Node.js is installed
if ! command -v node > /dev/null 2>&1; then
    echo "❌ Node.js is not installed. Please install Node.js and try again."
    exit 1
fi

# Check if Java is installed
if ! command -v java > /dev/null 2>&1; then
    echo "❌ Java is not installed. Please install Java 21+ and try again."
    exit 1
fi

# Start infrastructure (database and Kafka)
echo "🔨 Starting infrastructure (Database + Kafka)..."
docker-compose up postgres kafka -d

# Wait for services to be ready
echo "⏳ Waiting for infrastructure to start..."
sleep 5

# Start backend in development mode
echo "🚀 Starting backend..."
cd backend
./mvnw spring-boot:run &
BACKEND_PID=$!
cd ..

# Start frontend on port 3000 (same as Docker)
echo "🚀 Starting frontend on port 3000..."
cd frontend
npm install
npm run dev -- --port 3000 &
FRONTEND_PID=$!
cd ..

echo ""
echo "🎉 Development environment started!"
echo ""
echo "📱 Frontend: http://localhost:3000"
echo "🔧 Backend: http://localhost:8080"
echo "💾 Database: localhost:5432"
echo "📨 Kafka: localhost:9092"
echo ""
echo "To stop:"
echo "  Press Ctrl+C or run: kill $BACKEND_PID $FRONTEND_PID"
echo ""

# Wait for user to stop
wait