package com.cs4411;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

/* mvn exec:java -Dexec.mainClass="com.cs4411.KafkaProducerExample"
*/

public class KafkaProducerExample {
    public static void main(String[] args) {

        System.out.println("Starting Producer\n");

        // Set Kafka producer properties
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092"); // default Kafka broker address when using homebrew
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");


        Producer<String, String> producer = new KafkaProducer<>(props);

        String topic = "test-topic"; 
        String value = "Hello from Java Producer Example again (#2)!";
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, value);

        try {
            // Synchronous send
            RecordMetadata metadata = producer.send(record).get();
            System.out.printf("Sent record to topic %s partition %d with offset %d%n",
                    metadata.topic(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close(); // Close the producer to release resources
        }
    }
}
