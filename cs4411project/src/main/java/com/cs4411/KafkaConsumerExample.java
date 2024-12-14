package com.cs4411;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/* mvn exec:java -Dexec.mainClass="com.cs4411.KafkaConsumerExample"
*/

public class KafkaConsumerExample {

    public static void main(String[] args) {
        // consumer configuration settings
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");             // Consumer group ID
        props.put("enable.auto.commit", "true");         // set auto-commit offsets on 
        props.put("auto.commit.interval.ms", "1000");    // set commit interval
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("test-topic")); // subscribing to topic

        System.out.println("Starting Consumer\n");

        // continuously fetch (poll) for new messages

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // polling interval
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Topic: %s, Parition: %d, Offset: %d, Value: %s%n", 
                                      record.topic(), record.partition(), record.offset(), record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }
}
