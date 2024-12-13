package com.cs4411;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;




public class UserConsumer {
    public static void main(String[] args) {
        // Kafka consumer configuration
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "user-consumer-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        props.put("enable.auto.commit", "false");         // set auto-commit offsets on 


        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);


        consumer.subscribe(Collections.singletonList("user_changes")); // subscribing to topic

        


        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            records.forEach(record -> {
                System.out.println("Received message from User Changes: " + record.value());
                // 
            });
        }
    }
}
