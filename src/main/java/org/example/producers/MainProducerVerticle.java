package org.example.producers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class MainProducerVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.deployVerticle(new UserInputProducerVerticle());
        //vertx.deployVerticle(new FileEventProducerVerticle());
    }
}