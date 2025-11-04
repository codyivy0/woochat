# Kafka Dockerfile for Railway deployment with KRaft mode
FROM confluentinc/cp-kafka:7.5.0

# Create the KRaft configuration file
RUN mkdir -p /tmp/kraft-combined-logs && \
    echo "# KRaft mode configuration" > /tmp/kafka.properties && \
    echo "node.id=1" >> /tmp/kafka.properties && \
    echo "process.roles=broker,controller" >> /tmp/kafka.properties && \
    echo "controller.quorum.voters=1@0.0.0.0:29093" >> /tmp/kafka.properties && \
    echo "listeners=PLAINTEXT://0.0.0.0:9092,CONTROLLER://0.0.0.0:29093" >> /tmp/kafka.properties && \
    echo "inter.broker.listener.name=PLAINTEXT" >> /tmp/kafka.properties && \
    echo "controller.listener.names=CONTROLLER" >> /tmp/kafka.properties && \
    echo "listener.security.protocol.map=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT" >> /tmp/kafka.properties && \
    echo "advertised.listeners=PLAINTEXT://0.0.0.0:9092" >> /tmp/kafka.properties && \
    echo "offsets.topic.replication.factor=1" >> /tmp/kafka.properties && \
    echo "group.initial.rebalance.delay.ms=0" >> /tmp/kafka.properties && \
    echo "transaction.state.log.min.isr=1" >> /tmp/kafka.properties && \
    echo "transaction.state.log.replication.factor=1" >> /tmp/kafka.properties && \
    echo "log.dirs=/tmp/kraft-combined-logs" >> /tmp/kafka.properties

# Set cluster ID for KRaft
ENV CLUSTER_ID=MkU3OEVBNTcwNTJENDM2Qk

# Expose the ports
EXPOSE 9092

# Format storage and start Kafka in KRaft mode
CMD ["sh", "-c", "kafka-storage format -t $CLUSTER_ID -c /tmp/kafka.properties --ignore-formatted && kafka-server-start /tmp/kafka.properties"]