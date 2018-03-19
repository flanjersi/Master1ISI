package fr.master1ISI.databaseManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private DataSource dataSource;

    public DatabaseManager(String username, String password, String database) {
        init(username, password, database);
    }

    private void init(String username, String password, String database) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://localhost:3306/" + database + "?autoReconnect=true&useSSL=false");
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
        return dataSource.getConnection();
    }


}
