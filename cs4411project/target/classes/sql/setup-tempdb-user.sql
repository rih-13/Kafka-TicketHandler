CREATE DATABASE db2temp;
CREATE USER 'db2_user'@'localhost' IDENTIFIED BY 'uwodb2pwd';
SELECT User, Host FROM mysql.user WHERE User = 'db2_user';
GRANT ALL PRIVILEGES ON db2temp.* TO 'db2_user'@'localhost';
FLUSH PRIVILEGES;
USE db2temp;
CREATE TABLE User(
userID BigInt NOT NULL,
userName varchar(255) NOT NULL,
PRIMARY KEY (userID)
);
CREATE TABLE Ticket(
ticketID BigInt NOT NULL,
sold boolean NOT NULL,
userID BigInt, 
PRIMARY KEY (ticketID),
FOREIGN KEY (userID) REFERENCES User(userID)
);