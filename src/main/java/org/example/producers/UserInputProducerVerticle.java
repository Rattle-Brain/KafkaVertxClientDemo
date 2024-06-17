package org.example.producers;

import io.vertx.core.*;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.RecordMetadata;
import org.example.producers.configs.ProducerConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Scanner;

public class UserInputProducerVerticle extends AbstractVerticle {
    private Logger LOGGER = LoggerFactory.getLogger(UserInputProducerVerticle.class);
    private KafkaProducer<String, String> producer;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Map<String, String> config = ProducerConfigs.getGenericProducerConfig();
        producer = KafkaProducer.create(vertx, config, String.class, String.class);

        vertx.executeBlocking(promise -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();
                sendMessage(message);
            }
        }, res -> {
            if (res.succeeded()) {
                startPromise.complete();
            } else {
                startPromise.fail(res.cause());
            }
        });
    }

    private void sendMessage(String message) {
        KafkaProducerRecord<String, String> record = KafkaProducerRecord.create("user-input-topic", message);
        producer.send(record, asyncResult -> {
            if (asyncResult.succeeded()) {
                RecordMetadata rm = asyncResult.result();
                System.out.printf("Message sent to topic %s partition %d offset %d%n",
                        rm.getTopic(), rm.getPartition(), rm.getOffset());
            } else {
                System.err.println("Message failed to send: " + asyncResult.cause().getMessage());
            }
        });
    }

    @Override
    public void stop(Promise<Void> promise) throws Exception {
        LOGGER.info("Stopping Producer");
        if (producer != null) {
            producer.close();
        }
    }
}
