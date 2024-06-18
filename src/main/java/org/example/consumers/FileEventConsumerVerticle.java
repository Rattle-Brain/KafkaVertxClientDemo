package org.example.consumers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import org.example.TopicNames;
import org.example.consumers.configs.ConsumerConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileEventConsumerVerticle extends AbstractVerticle {


    private final Logger LOGGER = LoggerFactory.getLogger(FileEventConsumerVerticle.class);
    private final String topicName = TopicNames.FILE_EVENT_TOPIC;

    @Override
    public void start() throws Exception {

        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, ConsumerConfigs.genericConsumerConfig);

        // Subscribe to the topic
        consumer.subscribe(topicName, asyncResult -> {
            if (asyncResult.succeeded()) {
                System.out.println("Subscribed to " + topicName);
            } else {
                System.err.println("Failed to subscribe to " + topicName + ": " + asyncResult.cause().getMessage());
            }
        });

        consumer.handler(record -> {
            System.out.printf("Consumed message with key: %s, value: %s, from partition: %d, offset: %d%n",
                    record.key(), record.value(), record.partition(), record.offset());
        });
    }

    @Override
    public void stop(Promise<Void> promise) throws Exception{
        LOGGER.info("Stopping Consumer");
        super.stop(promise);
    }
}
