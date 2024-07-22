package org.example.producers.configs;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

public class ProducerConfigs {

    public static Map<String, String> genericProducerConfig = getGenericProducerConfig();

    public static Map<String, String> getGenericProducerConfig() {
        Map<String, String> config = new HashMap<>();

        config.put(BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(ACKS_CONFIG, "1");
        config.put(MAX_BLOCK_MS_CONFIG, "10000");

        return config;
    }
}
