package org.example.consumers;

import io.vertx.core.AbstractVerticle;
import org.example.producers.FileEventProducerVerticle;

public class MainConsumerVerticle extends AbstractVerticle {
    @Override
    public void start() {
        //vertx.deployVerticle(new UserInputConsumerVerticle());
        vertx.deployVerticle(new FileEventConsumerVerticle());
    }
}
