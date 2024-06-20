package org.example.producers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.file.FileSystem;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.example.TopicNames;
import org.example.producers.configs.ProducerConfigs;
import org.example.producers.utils.ProducerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileEventProducerVerticle extends AbstractVerticle {
    private static final String FILE_EVENT_ADDRESS = "file.event";
    private final Logger LOGGER = LoggerFactory.getLogger(FileEventProducerVerticle.class);
    private KafkaProducer<String, String> producer;
    private final String topicName = TopicNames.FILE_EVENT_TOPIC;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Map<String, String> config = ProducerConfigs.genericProducerConfig;
        producer = KafkaProducer.create(vertx, config);

        MessageConsumer<String> fileEvent = vertx.eventBus().consumer(FILE_EVENT_ADDRESS);
        fileEvent.handler(message -> {
            String fileEventMsg = message.body();
            ProducerUtils.sendMessageToBroker(producer, topicName, fileEventMsg);
        });

        fileEventWatchdog("/home/dani");

        startPromise.complete();
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception{
        LOGGER.info("Stopping Producer");
        if (producer != null) {
            producer.close();
        }
        super.stop(stopPromise);
        LOGGER.info("Producer Stopped");
    }

    private void fileEventWatchdog(String dirPath) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                FileSystem fs = vertx.fileSystem();
                WatchService watchService = FileSystems.getDefault().newWatchService();
                Path path = Paths.get(dirPath);
                path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);

                while (true) {
                    WatchKey key;
                    try {
                        key = watchService.take();
                    } catch (InterruptedException ex) {
                        executor.shutdownNow();
                        return;
                    }

                    for (WatchEvent<?> event : key.pollEvents()) {
                        WatchEvent.Kind<?> kind = event.kind();
                        Path fileName = (Path) event.context();
                        String fullPath = dirPath + "/" + fileName.toString();

                        if(fullPath.contains("swp"))
                            continue;
                        if(fullPath.contains(".xsession"))
                            continue;
                        if(fullPath.contains("_history.LOCK"))
                            continue;


                        String message = String.format("Event %s occurred on file %s", kind.name(), fullPath);

                        vertx.eventBus().send(FILE_EVENT_ADDRESS, message);
                    }

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }

                executor.shutdownNow();
            } catch (Exception e) {
                LOGGER.error("Exception occurred " + e.getMessage());
                executor.shutdownNow();
            }
        });
    }

}
