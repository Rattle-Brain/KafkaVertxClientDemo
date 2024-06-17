package org.example.consumers;

import io.vertx.core.AbstractVerticle;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.example.TopicNames;
import org.example.consumers.configs.ConsumerConfigs;

public class UserInputConsumerVerticle extends AbstractVerticle {

    private String topic_name = TopicNames.USER_INPUT_TOPIC;

    @Override
    public void start() {
        // Create a Kafka consumer
        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, ConsumerConfigs.genericConsumerConfig);

        // Subscribe to the topic
        consumer.subscribe(topic_name, asyncResult -> {
            if (asyncResult.succeeded()) {
                System.out.println("Subscribed to " + topic_name);
            } else {
                System.err.println("Failed to subscribe to " + topic_name + ": " + asyncResult.cause().getMessage());
            }
        });

        consumer.handler(record -> {
            System.out.printf("Consumed message with key: %s, value: %s, from partition: %d, offset: %d%n",
                    record.key(), record.value(), record.partition(), record.offset());
        });
    }
}
