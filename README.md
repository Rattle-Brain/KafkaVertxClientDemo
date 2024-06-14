# Vert.x Kafka Producer and Consumer

This project demonstrates a Kafka Producer and Consumer implementation using Vert.x, a reactive toolkit for the JVM. The producer reads input from the console in an infinite loop and sends each message to a Kafka topic, while the consumer reads messages from the topic and processes them. The architecture is designed to be modular to allow easy addition of new producers or consumers.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [Adding New Producers/Consumers](#adding-new-producersconsumers)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Modular Architecture**: Easily extend the project with additional producers and consumers.
- **Configuration**: Externalized Kafka configuration for ease of management.

## Prerequisites

- **Java 17 or higher**: Ensure you have JDK 17 or later installed.
- **Apache Kafka**: Make sure Kafka is running locally or adjust configurations to point to your Kafka server.
- **Maven**: For dependency management and building the project.

## Installation

1. **Clone the Repository**:

    ```bash
    git clone https://github.com/Rattle-Brain/KafkaVertxClientDemo.git
    cd KafkaVertxClientDemo
    ```

2. **Build the Project**:

    ```bash
    mvn clean install
    ```

## Usage

### Running the Producer

To start the Kafka producer:

```bash
mvn exec:java -Dexec.mainClass="com.example.kafkaproducer.MainVerticle"
```

This will start the Vert.x application and read input from the console, sending each message to the configured Kafka topic.

### Running the Consumer

To start the Kafka consumer:

```bash
mvn exec:java -Dexec.mainClass="com.example.kafkaconsumer.MainConsumerVerticle"
```

This will start the consumer that reads messages from the Kafka topic and processes them.

Project Structure

```plaintext
KafkaVertxClientDemo/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── kafkaproducer/
│   │   │           │   ├── MainVerticle.java
│   │   │           │   ├── KafkaProducerVerticle.java
│   │   │           │   ├── ConsoleInputHandler.java
│   │   │           │   └── KafkaConfig.java
│   │   │           └── kafkaconsumer/
│   │   │               ├── MainConsumerVerticle.java
│   │   │               ├── KafkaConsumerVerticle.java
│   │   │               └── KafkaConfig.java
│   │   └── resources/
│   │       └── logback.xml
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   ├── kafkaproducer/
│                   └── kafkaconsumer/
└── pom.xml
```

- MainVerticle.java: Entry point for the producer.
- KafkaProducerVerticle.java: Kafka producer logic.
- ConsoleInputHandler.java: Handles reading input from the console.
- KafkaConfig.java: Kafka producer configuration.
- MainConsumerVerticle.java: Entry point for the consumer.
- KafkaConsumerVerticle.java: Kafka consumer logic.

## Adding New Producers/Consumers
### Adding a New Producer

Create a new Verticle class for the producer (e.g., NewKafkaProducerVerticle.java).

Implement the producer logic.

Deploy the new producer verticle in MainVerticle:

```java
vertx.deployVerticle(new NewKafkaProducerVerticle());
```

### Adding a New Consumer

Create a new Verticle class for the consumer (e.g., NewKafkaConsumerVerticle.java).

Implement the consumer logic.

Deploy the new consumer verticle in MainConsumerVerticle:

```java
vertx.deployVerticle(new NewKafkaConsumerVerticle());
```

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

1. Fork the repository.
2. Create your feature branch (git checkout -b feature/AmazingFeature).
3. Commit your changes (git commit -m 'Add some AmazingFeature').
4. Push to the branch (git push origin feature/AmazingFeature).
5. Open a pull request.

## License

This project is licensed under the [Apache 2.0 License](LICENSE).

# DISCLAIMER

This Readme has been generated with AI
