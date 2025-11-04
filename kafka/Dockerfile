# Kafka Dockerfile for Railway deployment with KRaft mode
FROM confluentinc/cp-kafka:7.5.0

# Create KRaft configuration and log directory
RUN mkdir -p /opt/kafka/config /tmp/kraft-combined-logs && \
    echo "# KRaft Configuration" > /opt/kafka/config/server.properties && \
    echo "node.id=1" >> /opt/kafka/config/server.properties && \
    echo "process.roles=broker,controller" >> /opt/kafka/config/server.properties && \
    echo "controller.quorum.voters=1@localhost:29093" >> /opt/kafka/config/server.properties && \
    echo "listeners=PLAINTEXT://:9092,CONTROLLER://:29093,EXTERNAL://:9094" >> /opt/kafka/config/server.properties && \
    echo "inter.broker.listener.name=PLAINTEXT" >> /opt/kafka/config/server.properties && \
    echo "controller.listener.names=CONTROLLER" >> /opt/kafka/config/server.properties && \
    echo "listener.security.protocol.map=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,EXTERNAL:PLAINTEXT" >> /opt/kafka/config/server.properties && \
    echo "advertised.listeners=PLAINTEXT://localhost:9092,EXTERNAL://0.0.0.0:9094" >> /opt/kafka/config/server.properties && \
    echo "offsets.topic.replication.factor=1" >> /opt/kafka/config/server.properties && \
    echo "group.initial.rebalance.delay.ms=0" >> /opt/kafka/config/server.properties && \
    echo "transaction.state.log.replication.factor=1" >> /opt/kafka/config/server.properties && \
    echo "transaction.state.log.min.isr=1" >> /opt/kafka/config/server.properties && \
    echo "log.dirs=/tmp/kraft-combined-logs" >> /opt/kafka/config/server.properties

# Expose the ports
EXPOSE 9092 9094

# Format log directories for KRaft and start Kafka
CMD ["sh", "-c", "kafka-storage format -t $CLUSTER_ID -c /opt/kafka/config/server.properties --ignore-formatted && kafka-server-start /opt/kafka/config/server.properties"]