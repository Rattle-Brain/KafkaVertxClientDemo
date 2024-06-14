package org.example.consumers.configs;

import java.util.HashMap;
import java.util.Map;

public class ConsumerConfigs {

    public static Map<String, String> genericConsumerConfig = getGenericConsumerConfig();

    public static Map<String, String> getGenericConsumerConfig() {
        Map<String, String> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("group.id", "vertx-group");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("auto.offset.reset", "earliest");
        return config;
    }
}
