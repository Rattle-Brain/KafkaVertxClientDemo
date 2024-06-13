package org.example.producers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {
    public static void main(String[] args) {
        System.out.println("Initializing Vertx");
        Vertx v = Vertx.vertx();
        v.deployVerticle(new MainVerticle());
    }

    @Override
    public void start() {
        vertx.deployVerticle(new UserInputProducerVerticle());
    }
}