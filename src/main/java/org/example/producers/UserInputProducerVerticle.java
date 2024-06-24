package org.example.producers;

import io.vertx.core.*;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.example.TopicNames;
import org.example.producers.configs.ProducerConfigs;
import org.example.producers.utils.ProducerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.example.schemaRegistry.*;

/**
 * This class blocks and produces a Vert.x Blocking Exception. This behaviour is expected.
 *
 * However, by using an extra worker thread written in plain Java, you can block that thread
 * indefinitely and, by using the Vert.x Event Bus, we send the event to the Vert.x thread so
 * it can publish the information to the Kafka Topic.
 */
public class UserInputProducerVerticle extends AbstractVerticle {
    private static final String USER_INPUT_ADDRESS = "user.input";
    private final Logger LOGGER = LoggerFactory.getLogger(UserInputProducerVerticle.class);
    private KafkaProducer<String, String> producer;
    private final String topicName = TopicNames.USER_INPUT_TOPIC;

    @Override
    public void start(Promise<Void> promise) throws Exception {
        Map<String, String> config = ProducerConfigs.genericProducerConfig;
        producer = KafkaProducer.create(vertx, config, String.class, String.class);

        MessageConsumer<String> userInputConsumer = vertx.eventBus().consumer(USER_INPUT_ADDRESS);
        userInputConsumer.handler(message -> {
            String userInputMsg = message.body();
            ProducerUtils.sendMessageToBroker(producer, topicName, userInputMsg);
        });

        startConsoleInputThread();

        promise.complete();
    }

    private void startConsoleInputThread() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.submit(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter message: ");
            while (true) {
                String message = scanner.nextLine();
                if (message.toLowerCase().equals("exit")) {
                    System.out.println("Exiting Producer...");
                    break;
                }

                message = jsonifyUserInput(message);

                vertx.eventBus().send(USER_INPUT_ADDRESS, message);
            }

            executorService.shutdownNow();
        });
    }

    private String jsonifyUserInput(String message) {
        try {
            String user = System.getProperty("user.name");
            // Get the schema from registry
            Schema schema = SchemaRegistryUtils.retrieveSchemaFromRegistry(SchemaRegistryUtils.UINPUT_TOPIC_SCHEMA_URL);
            GenericRecord avroRecord = new GenericData.Record(schema);

            // Fill the fields of the avro schema with data
            avroRecord.put("user", user);
            avroRecord.put("msg", message);
            avroRecord.put("topicName", topicName);

            return avroRecord.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void stop(Promise<Void> promise) throws Exception {
        LOGGER.info("Stopping Producer");
        if (producer != null) {
            producer.close();
        }
        super.stop(promise);
        LOGGER.info("Producer Stopped");
    }
}
