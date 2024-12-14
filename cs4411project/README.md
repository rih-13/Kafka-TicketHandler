# Apache Ticket

These setup and run instructions are for Mac systems, which Apache Kafka works best on as it is designed for Linux based systems. You can run the program on Windows by using WSL to emulate Linux and configuring the zookeeper.properties and server.properties files and using the equivalent Windows commands.

For ease of installation and use, we use Homebrew for package management. Homebrew can be installed for free, or you can use the equivalent terminal commands.

## Setup Maven

From the project directory, open a terminal and run  
mvn clean install  
mvn compile

## Setup Zookeeper, Kafka, MySql

From the terminal, start MySQL and log in by running /opt/homebrew/opt/mysql/bin/mysqld_safe --datadir\=/opt/homebrew/var/mysql  
From a new terminal, start Zookeeper by running SERVER_JVMFLAGS="-Dapple.awt.UIElement=true" /opt/homebrew/opt/zookeeper/bin/zkServer start-foreground  
From another new terminal, start Kafka by running /opt/homebrew/opt/kafka/bin/kafka-server-start /opt/homebrew/etc/kafka/server.properties

- From this terminal you can also interact with Kafka and see changes you made.
- To list all topics you can use the command /opt/homebrew/opt/kafka/bin/kafka-topics --list --bootstrap-server localhost:9092
- A full list of Kafka CLI commands can be found using ls /opt/homebrew/opt/kafka/bin/
- For more information on particular commands, use the help option ex. for commands relating to topics run kafka-topics --help

## Create Kafka Topics

As mentioned in the project, you can create a Kafka topic with variable partitions, replication, etc.

For basic Kafka usage, 1 partition and no replication run:  
/opt/homebrew/opt/kafka/bin/kafka-topics --create --topic user_changes --bootstrap-server localhost:9092  
/opt/homebrew/opt/kafka/bin/kafka-topics --create --topic ticket_changes --bootstrap-server localhost:9092

For Kafka usage with 2 partitions and no replication run (preferred/easiest):  
/opt/homebrew/opt/kafka/bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic user_changes  
/opt/homebrew/opt/kafka/bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic ticket_changes

Another way to run Kafka usage with 2 partitions and no replication (method used in demonstration):  
Create two new topics with different names,  
/opt/homebrew/opt/kafka/bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic user_changes2  
/opt/homebrew/opt/kafka/bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 2 --topic ticket_changes2  
Important: Ensure that the DatabaseProducer, UserConsumer, and TicketConsumer Java files matche the topic names that were created. You can change the name of the topics in these three files and then recompile to match the name of the topic in Kafka.
Files DatabaseProducerPartitioned, UserConsumerPartitioned, and TicketConsumerPartitioned have been provided which are preset to produce/consume from topics "user_changes2" and "ticket_changes2".

## Start the Program

Set up database, run: mysql -u root -p < src/main/resources/sql/setup-tempdb-user.sql  
Upload data to SQL server, run: mvn exec:java -Dexec.mainClass="com.cs4411.SQLServerJDBCConnection"  
Send data to Kafka, run: mvn exec:java -Dexec.mainClass="com.cs4411.DatabaseProducer"  
Consume (print) user data from Kafka, run: mvn exec:java -Dexec.mainClass="com.cs4411.UserConsumer"  
Consume (print) user data from Kafka, run: mvn exec:java -Dexec.mainClass="com.cs4411.TicketConsumer"  
Remove databsel, run mysql -u root -p < src/main/resources/sql/remove-tempdb-user.sql
