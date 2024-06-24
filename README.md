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

### 1. **Clone the Repository**:

    ```bash
    git clone https://github.com/Rattle-Brain/KafkaVertxClientDemo.git
    cd KafkaVertxClientDemo
    ```

### 2. **Build the Project**:

    ```bash
    mvn clean install
    ```

### 3. **Kafka Cluster Setup**

If you already have an operational Kafka cluster, you may skip this step. Otherwise, follow these instructions to set up a Kafka cluster using Strimzi.

Refer to the [Strimzi Quickstart Guide](https://strimzi.io/quickstarts) for comprehensive documentation. For this setup, we have utilized the Strimzi quickstart process, but the essential steps are outlined below:

#### Step 1: Create the Namespace

Create a dedicated namespace for Kafka:

   ```bash
   kubectl create namespace kafka
   ```

#### Step 2: Deploy the Cluster Operator and Kafka Cluster

Apply the Strimzi cluster operator and the Kafka cluster configuration:

   ```bash
   kubectl create -f ./kafka-yamls/strimzi-quickstart.yaml
   kubectl apply -f ./kafka-yamls/kafka-cluster.yaml
   ```

**Note:** The `strimzi-quickstart.yaml` file can be obtained from the [Strimzi installation page](https://strimzi.io/install/latest?namespace=kafka). Please refer to the [Strimzi Quickstart Guide](https://strimzi.io/quickstarts) for further information. The Kafka cluster has been **configured from scratch** according to this guide.

#### Step 3: Access the Kafka Bootstrap Service

Once the deployment is complete, wait for all resources to be fully created. To access the Kafka bootstrap service, use the following command:

   ```bash
   kubectl -n kafka port-forward services/strimzi-kafka-cluster-kafka-external-bootstrap 9092:9094 &
   ```

This command should establish the port-forwarding. If you encounter a connection refusal from the container, please wait a bit longer as the resources may still be initializing.

### 4. Avro schema registry setup

#### Step 1: Deploy the Schema Registry

Apply the Schema Registry configuration provided:

   ```bash
   kubectl apply -f ./kafka-yamls/schema-registry.yaml
   ```

#### Step 3: Access the Avro Schema Registry

Once the deployment is complete, wait for all resources to be fully created. To allow access to the Schema Registry:

   ```bash
   kubectl -n kafka port-forward services/schema-reg 8090:8090 &
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

This project is licensed under the [MIT](LICENSE).

# DISCLAIMER

This Readme has been generated with AI
