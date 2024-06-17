package org.example.consumers;

import io.vertx.core.AbstractVerticle;

public class MainConsumerVerticle extends AbstractVerticle {
    @Override
    public void start() {
        vertx.deployVerticle(new UserInputConsumerVerticle());
    }
}
