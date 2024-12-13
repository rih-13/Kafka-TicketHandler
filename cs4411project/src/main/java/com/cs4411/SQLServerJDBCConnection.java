package com.cs4411;

import java.sql.*;
import java.util.HashSet;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SQLServerJDBCConnection {
    static long ticketSize = 1000L;
    static long userSize = 500L;
    static boolean dataLoaded = false;

    public static void runSQLScript(Connection con, String sqlFilePath) throws Exception {
        InputStream inputStream = SQLServerJDBCConnection.class.getResourceAsStream("/sql/" + sqlFilePath);
        String sql = new String(inputStream.readAllBytes());
        Statement statement = con.createStatement();
        statement.execute(sql);
    }

    public static void insertUser(Connection con, User u) throws SQLException {
       PreparedStatement ps = con.prepareStatement("INSERT INTO user VALUES (?, ?);");
       ps.setLong(1, u.userID);
       ps.setString(2, u.getName());
       ps.execute();
    }

    public static void insertTicket(Connection con, Ticket t) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO ticket VALUES (?, ?, ?);");
        ps.setLong(1, t.ticketID);
        ps.setBoolean(2, t.isSold());
        Long uid = t.getUser();
        if (uid == -255L){
            uid = null;
            ps.setNull(3, 0);
        }
        else{
            ps.setLong(3, uid);
        }
        ps.execute();
    }

    public static void loadTicketDataset(Connection con, HashSet<Ticket> dat) throws SQLException {
        for (Ticket t: dat){
            insertTicket(con, t);
        }
    }

    public static void loadUserDataset(Connection con, HashSet<User> dat) throws SQLException {
        for (User u: dat){
            insertUser(con, u);
        }
    }


    public static PreparedStatement genStatement(Connection con, String stmStr ){
        return null;
    }

    static DataGenerator.UserDataGenerator userGen;
    static DataGenerator.TicketDataGenerator ticketGen;



    public static void main(String[] args) {
        // JDBC URL for SQL Server (using Windows Authentication)
        userGen = DataGenerator.UserDataGenerator.getUserDataGenerator(userSize);
        ticketGen = DataGenerator.TicketDataGenerator.getTicketDataGenerator(ticketSize);


        String username = "db2_user";
        String userpwd = "uwodb2pwd";
        String dbname = "db2temp";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbname,username,userpwd);

            //runSQLScript(conn, "setup-tempdb-user.sql");
            

            // Example preparedstatement here
            /*
            PreparedStatement ps1 = conn.prepareStatement("SELECT * FROM user WHERE userID = ?;");
            ps1.setString(1,Long.toString(0L));
            ResultSet r1 = ps1.executeQuery();
            while (r1.next()){
                System.out.println(r1.getString("userID"));
            }
            */
            if (!dataLoaded) {
                HashSet<User> userdat = userGen.generateDataset();
                HashSet<Ticket> ticketdat = ticketGen.generateDataset();
                loadTicketDataset(conn, ticketdat);
                loadUserDataset(conn, userdat);
            }

            // Get the total count of entries and the first 5 entries for the User table
            // Print User table info
            PreparedStatement userCountPS = conn.prepareStatement("SELECT COUNT(*) AS userCount FROM User;");
            ResultSet userCountRS = userCountPS.executeQuery();
            if (userCountRS.next()) {
                System.out.println("Total entries in User table: " + userCountRS.getInt("userCount"));
            }
            userCountRS.close();
            userCountPS.close();

            PreparedStatement first5UsersPS = conn.prepareStatement("SELECT * FROM User LIMIT 5;");
            ResultSet first5UsersRS = first5UsersPS.executeQuery();
            System.out.println("First 5 entries in User table:");
            while (first5UsersRS.next()) {
                System.out.println("userID: " + first5UsersRS.getLong("userID") + 
                                ", userName: " + first5UsersRS.getString("userName"));
            }
            first5UsersRS.close();
            first5UsersPS.close();

            // Print Ticket table info
            PreparedStatement ticketCountPS = conn.prepareStatement("SELECT COUNT(*) AS ticketCount FROM Ticket;");
            ResultSet ticketCountRS = ticketCountPS.executeQuery();
            if (ticketCountRS.next()) {
                System.out.println("Total entries in Ticket table: " + ticketCountRS.getInt("ticketCount"));
            }
            ticketCountRS.close();
            ticketCountPS.close();

            PreparedStatement first5TicketsPS = conn.prepareStatement("SELECT * FROM Ticket LIMIT 5;");
            ResultSet first5TicketsRS = first5TicketsPS.executeQuery();
            System.out.println("First 5 entries in Ticket table:");
            while (first5TicketsRS.next()) {
                System.out.println("ticketID: " + first5TicketsRS.getLong("ticketID") +
                                ", sold: " + first5TicketsRS.getBoolean("sold") +
                                ", userID: " + first5TicketsRS.getLong("userID"));
            }
            first5TicketsRS.close();
            first5TicketsPS.close();


            //runSQLScript(conn, "remove-tempdb-user.sql");


            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

