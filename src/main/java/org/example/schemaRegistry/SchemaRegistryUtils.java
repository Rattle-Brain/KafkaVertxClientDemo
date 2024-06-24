package com.kafka.producer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.avro.Schema;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class SchemaRegistryUtils {

    public static final String SCHEMA_URL = "http://localhost:8090/subjects/json-file-events/versions/1";

    /**
     * Tries to connect to a schema registry by url and retrieve a schema for later use.
     *
     * @param schemaRegistryUrl URL of the schema registry
     * @return the Schema retrieved as an Apache Avro Schema
     * @throws IOException if something went wrong while retrieving the schema
     */
    public static Schema retrieveSchemaFromRegistry(String schemaRegistryUrl) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet(schemaRegistryUrl);
            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String result = EntityUtils.toString(entity);
                String schemaJson = getSchemaFromJSON(result);

                // Parse the JSON schema string into an Avro Schema object
                return new Schema.Parser().parse(schemaJson);
            } else {
                throw new IOException("Failed to retrieve schema from Schema Registry."+
                        " Status code: " + response.getStatusLine().getStatusCode());
            }
        } finally {
            httpClient.close();
        }
    }

    /**
     * Obtains the actual schema definition from the JSON response retrieved from the server
     *
     * @param jsonResponse String with the JSON Response
     * @return String with just the schema definition
     * @throws IOException
     */
    public static String getSchemaFromJSON(String jsonResponse) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Extract the Avro schema string from the "schema" field
        JsonNode schemaNode = root.get("schema");
        if (schemaNode != null) {
            System.err.println("schemaNode: " + schemaNode.asText());
            return schemaNode.asText();
        } else {
            throw new IllegalArgumentException("Avro schema not found in the JSON response");
        }
    }

}
