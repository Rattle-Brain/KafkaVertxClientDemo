package org.example;

import io.vertx.core.Vertx;
import org.example.consumers.MainConsumerVerticle;
import org.example.producers.MainProducerVerticle;

public class Application {

    public static void main(String[] args) {
        System.out.println("Initializing Vertx");
        Vertx v = Vertx.vertx();
        v.deployVerticle(new MainProducerVerticle());
        //v.deployVerticle(new MainConsumerVerticle());
    }
}
