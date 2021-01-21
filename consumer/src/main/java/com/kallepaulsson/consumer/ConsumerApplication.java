package com.kallepaulsson.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.messaging.handler.annotation.Payload;

@SpringBootApplication
public class ConsumerApplication {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    public static final String TOPIC = "timestamps";

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = TOPIC,
            partitions = "#{@finder.partitions(T(com.kallepaulsson.consumer.ConsumerApplication).TOPIC)}",
            partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0")),
            groupId = "foo")
    public void listener(@Payload String message) {
        LOG.info(String.format("Received timestamp: %s", message));
    }

    @Bean
    public PartitionFinder finder(ConsumerFactory<String, String> consumerFactory) {
        return new PartitionFinder(consumerFactory);
    }

    public static class PartitionFinder {
        private final ConsumerFactory<String, String> consumerFactory;

        public PartitionFinder(ConsumerFactory<String, String> consumerFactory) {
            this.consumerFactory = consumerFactory;
        }

        public String[] partitions(String topic) {
            try (Consumer<String, String> consumer = consumerFactory.createConsumer()) {
                return consumer.partitionsFor(topic).stream()
                        .map(pi -> "" + pi.partition())
                        .toArray(String[]::new);
            }
        }
    }
}
