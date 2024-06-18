package org.example.producers.utils;

import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.RecordMetadata;
import io.vertx.kafka.client.producer.KafkaProducer;

public class ProducerUtils {

    public static void sendMessageToBroker(KafkaProducer<String, String> producer, String topicName, String msg){
        KafkaProducerRecord<String, String> record = KafkaProducerRecord.create(topicName, msg);
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
}
