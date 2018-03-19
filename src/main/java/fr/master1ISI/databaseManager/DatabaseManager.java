package fr.master1ISI.databaseManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.master1ISI.wrapperConception2.ConfigurationWrapper;
import fr.master1ISI.wrapperConception2.WrapperCSVDynamics;
import javafx.concurrent.Task;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {

    private DataSource dataSource;

    private final String path = "/home/jeremy/Faculte/Master/S2/ISI/";

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

    public Task<Integer> createAndInsertDataSrc1(){


        try {
            ConfigurationWrapper configurationWrapperS1 = new ConfigurationWrapper(new File(path + "Master1ISI/src/main/ressources/dataset/source1/globalterrorismdb_0617dist.csv"));
            configurationWrapperS1.setNameTable("SOURCE_1");

            WrapperCSVDynamics wrapper = new WrapperCSVDynamics(configurationWrapperS1, getConnection());
            configurationWrapperS1.setNbRowsData(93847);

            return wrapper;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }

    }


    public Task<Integer> createAndInsertDataSrc2(){

        try {
            ConfigurationWrapper configurationWrapperS2 = new ConfigurationWrapper(new File(path + "Master1ISI/src/main/ressources/dataset/source2/hates_crimes.csv"));
            configurationWrapperS2.setNameTable("SOURCE_2");

            WrapperCSVDynamics wrapper = new WrapperCSVDynamics(configurationWrapperS2, getConnection());
            configurationWrapperS2.setNbRowsData(52);

            return wrapper;

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }

    }

    public Task<Integer> createAndInsertDataSrc3(){

        try {
            ConfigurationWrapper configurationWrapperS3 = new ConfigurationWrapper(new File(path + "Master1ISI/src/main/ressources/dataset/source3/police_killings.csv"));
            configurationWrapperS3.setNameTable("SOURCE_3");
            configurationWrapperS3.setNbRowsData(468);

            WrapperCSVDynamics wrapper = new WrapperCSVDynamics(configurationWrapperS3, getConnection());

            return wrapper;

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }

    }

    public Task<Integer> createAndInsertDataSrc4(){


        try {
            ConfigurationWrapper configurationWrapperS4 = new ConfigurationWrapper(new File(path + "Master1ISI/src/main/ressources/dataset/source4/clean_data.csv"));
            configurationWrapperS4.setNameTable("SOURCE_4");
            configurationWrapperS4.setNbRowsData(22801);

            WrapperCSVDynamics wrapper = new WrapperCSVDynamics(configurationWrapperS4, getConnection());

            return wrapper;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }

    }

    public Task<Integer> createAndInsertDataSrc5(){


        try {
            ConfigurationWrapper configurationWrapperS5 = new ConfigurationWrapper(new File(path + "Master1ISI/src/main/ressources/dataset/source5/murder_2015_final.csv"));
            configurationWrapperS5.setNameTable("SOURCE_5");
            configurationWrapperS5.setNbRowsData(84);

            WrapperCSVDynamics wrapper = new WrapperCSVDynamics(configurationWrapperS5, getConnection());

            return wrapper;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public Task<Integer> createAndInsertDataSrc6(){
        try {

            ConfigurationWrapper configurationWrapperS5N2 = new ConfigurationWrapper(new File(path + "Master1ISI/src/main/ressources/dataset/source5/murder_2016_prelim.csv"));
            configurationWrapperS5N2.setNameTable("SOURCE_5N2");
            configurationWrapperS5N2.setNbRowsData(80);

            WrapperCSVDynamics wrapper = new WrapperCSVDynamics(configurationWrapperS5N2, getConnection());


            return wrapper;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

}
