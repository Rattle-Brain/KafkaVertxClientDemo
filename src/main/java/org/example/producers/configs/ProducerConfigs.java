package org.example.producers.configs;

import java.util.HashMap;
import java.util.Map;

public class ProducerConfigs {

    public static Map<String, String> genericProducerConfig = getGenericProducerConfig();

    public static Map<String, String> getGenericProducerConfig() {
        Map<String, String> config = new HashMap<>();

        config.put("bootstrap.servers", "localhost:8092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "1");

        return config;
    }
}
