package fr.master1ISI.databaseManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private DataSource dataSource;


    private String database;
    private String password;
    private String username;

    public DatabaseManager(String username, String password, String database) {
        this.password = password;
        this.username = username;
        this.database = database;
        //init(username, password, database);
    }

    private void init(String username, String password, String database) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://localhost:3306/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(20);
        config.setAutoCommit(true);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);
    }


    public synchronized Connection getConnection() throws SQLException {
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //return dataSource.getConnection();
    }

    /*
    private static void create_table() throws Exception {
        System.out.println("Creating table in given database...");
        stmt = conn.createStatement();

        String sql = "CREATE TABLE REGISTRATION " + "(id INTEGER not NULL, "
                + " first VARCHAR(255), " + " last VARCHAR(255), "
                + " age INTEGER, " + " PRIMARY KEY ( id ))";

        stmt.executeUpdate(sql);
        System.out.println("Table created in given database.");
    }

    private static void insert_values() throws Exception {
        System.out.println("Inserting records into the table...");
        stmt = conn.createStatement();

        String sql = "INSERT INTO Registration "
                + "VALUES (100, 'Zara', 'Ali', 18)";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO Registration "
                + "VALUES (101, 'Mahnaz', 'Fatma', 25)";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO Registration " + "VALUES (102, 'Zaid', 'Khan', 30)";
        stmt.executeUpdate(sql);
        sql = "INSERT INTO Registration "
                + "VALUES(103, 'Sumit', 'Mittal', 28)";
        stmt.executeUpdate(sql);
        System.out.println("Inserted records into the table...");
    }

    private static void display_values() throws Exception {
        System.out.println("Displaying values...");
        stmt = conn.createStatement();

        String sql = "SELECT id, first, last, age FROM Registration";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            // Retrieve by column name
            int id = rs.getInt("id");
            int age = rs.getInt("age");
            String first = rs.getString("first");
            String last = rs.getString("last");

            // Display values
            System.out.print("ID: " + id);
            System.out.print(", Age: " + age);
            System.out.print(", First: " + first);
            System.out.println(", Last: " + last);
        }
        rs.close();
    }

    private static void update_values() throws Exception {
        System.out.println("Updating values...");
        stmt = conn.createStatement();
        String sql = "UPDATE Registration "
                + "SET age = 30 WHERE id in (100, 101)";
        stmt.executeUpdate(sql);
        System.out.println("Values updated.");
    }

    private static void delete_values() throws Exception {
        System.out.println("Deleting some data...");
        stmt = conn.createStatement();
        String sql = "DELETE FROM Registration " + "WHERE id = 101";
        stmt.executeUpdate(sql);
        System.out.println("Data deleted.");
    }

    private static void close_and_save_database() throws Exception {
        // Close connexion to the database and save changes
        System.out.println("Closing database ...");
        Statement statement = conn.createStatement();
        statement.executeQuery("SHUTDOWN");
        statement.close();
        conn.close();
        System.out.println("Database closed and changes saved!");
    } */

}
