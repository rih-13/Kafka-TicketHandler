/** Sends messages to topic user_changes */


package com.cs4411;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.sql.*;

import java.util.Properties;
import java.util.Collections; // for subscribing to topics 
import java.util.List; 


public class DatabaseProducer {
    public static void main(String[] args) throws Exception {
        // Kafka producer configuration
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        String url = "jdbc:mysql://localhost:3306/db2temp";
        String username = "db2_user";
        String password = "uwodb2pwd";
        Connection conn = DriverManager.getConnection(url, username, password);

        // query User table
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM User");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String userID = String.valueOf(rs.getLong("userID"));
            String userName = rs.getString("userName");
            String message = "{\"operation\":\"INSERT\", \"table\":\"User\", \"data\":{\"userID\":\"" + userID + "\",\"userName\":\"" + userName + "\"}}";
            producer.send(new ProducerRecord<>("user_changes", userID, message));
            //producer.send(new ProducerRecord<>("user_changes2", userID, message)); You can change the topic name if needed
            System.out.println("Sent: " + message);
        }

        // query Ticket table
        ps = conn.prepareStatement("SELECT * FROM Ticket");
        rs = ps.executeQuery();
        while (rs.next()) {
            String ticketID = String.valueOf(rs.getLong("ticketID"));
            String sold = String.valueOf(rs.getBoolean("sold"));
            String userID = String.valueOf(rs.getLong("userID"));
            String message = "{\"operation\":\"INSERT\", \"table\":\"Ticket\", \"data\":{\"ticketID\":\"" + ticketID + "\",\"sold\":\"" + sold + "\",\"userID\":\"" + userID + "\"}}";
            //producer.send(new ProducerRecord<>("ticket_changes2", ticketID, message));
            producer.send(new ProducerRecord<>("ticket_changes", ticketID, message));
            System.out.println("Sent: " + message);
        }

        // Close resources
        producer.close();
        conn.close();
    }
}
