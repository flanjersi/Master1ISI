package fr.master1ISI.databaseManager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import fr.master1ISI.wrapperConception2.ConfigurationWrapper;
import fr.master1ISI.wrapperConception2.WrapperCSVDynamics;
import javafx.concurrent.Task;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private DataSource dataSource;


    public DatabaseManager(String username, String password, String database) {
        init(username, password, database);
    }

    private void init(String username, String password, String database) {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:mysql://localhost:3306/" + database + "?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
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

    public void sendRequest(final String request){

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute(request);
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendRequestAndPrintResult(final String request){

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(request);

            printResultSet(resultSet);

            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void printResultSet(ResultSet resultSet){
        try {
            resultSet.first();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnsNumber = resultSetMetaData.getColumnCount();

            int lengthString = 25;

            for(int i = 1 ; i <= columnsNumber ;  i++){
                System.out.print("| " + StringUtils.center(resultSetMetaData.getColumnName(i), lengthString) + " | ");
            }

            System.out.println("");

            for(int i = 1 ; i <= columnsNumber ;  i++){
                System.out.print("| " + StringUtils.repeat('-', lengthString) + " | ");
            }

            System.out.println("");


            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = resultSet.getString(i);
                    if(columnValue == null){
                        System.out.print("| " + StringUtils.center("null", lengthString) + " | ");
                    }
                    else {
                        System.out.print("| " + StringUtils.center(columnValue, lengthString) + " | ");
                    }
                }

                System.out.println("");

                for(int i = 1 ; i <= columnsNumber ;  i++){
                    System.out.print("| " + StringUtils.repeat('-', lengthString) + " | ");
                }

                System.out.println("");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public List<String> getAllCountry(){
        List<String> countries = new ArrayList<String>();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DISTINCT Country FROM CITIES WHERE Country IS NOT NULL ORDER BY Country");

            while(resultSet.next()){
                countries.add(resultSet.getString("Country"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return countries;
    }

    public List<String> getAllState(String country){
        List<String> states = new ArrayList<String>();

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT State FROM CITIES WHERE State IS NOT NULL AND Country = ? ORDER BY State");
            statement.setString(1, country);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                states.add(resultSet.getString("State"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return states;
    }


    public List<String> getAllRegions(String country){
        List<String> regions = new ArrayList<String>();

        try {
            Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT Region FROM CITIES WHERE Regions IS NOT NULL AND Country = ? ORDER BY Region");
            statement.setString(1, country);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                regions.add(resultSet.getString("Region"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return regions;
    }


    public void createViewMurders(){
        sendRequest("DROP VIEW IF EXISTS MURDERS");
        sendRequest("CREATE VIEW MURDERS\n" +
                "AS \n" +
                "\n" +
                "SELECT\n" +
                "SOURCE_1.iyear_SOURCE_1 AS iyear,\n" +
                "SOURCE_1.country_txt_SOURCE_1 AS country,\n" +
                "SOURCE_1.provstate_SOURCE_1 AS state,\n" +
                "SOURCE_1.city_SOURCE_1 AS city,\n" +
                "SOURCE_1.attacktype1_txt_SOURCE_1 AS attacktype\n" +
                "FROM SOURCE_1\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT\n" +
                "SOURCE_3.year_SOURCE_3 AS iyear,\n" +
                "'United States' AS country,\n" +
                "SOURCE_3.state_SOURCE_3 AS state,\n" +
                "SOURCE_3.city_SOURCE_3 \tAS city,\n" +
                "SOURCE_3.cause_SOURCE_3 AS attacktype\n" +
                "\n" +
                "FROM SOURCE_3\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT\n" +
                "SOURCE_4.year_SOURCE_4 AS iyear,\n" +
                "NULL AS country,\n" +
                "SOURCE_4.state_SOURCE_4 AS state,\n" +
                "NULL AS city,\n" +
                "SOURCE_4.cause_short_SOURCE_4 AS attacktype\n" +
                "\n" +
                "FROM SOURCE_4");
    }

    public void createViewMurdersStatistics(){
        sendRequest("DROP VIEW IF EXISTS MURDERS_STATISTICS");
        sendRequest("CREATE VIEW MURDERS_STATISTICS\n" +
                "AS SELECT \n" +
                "NULL AS city ,\n" +
                "SOURCE_2.state_SOURCE_2 AS state,\n" +
                "NULL AS 2014_murders,\n" +
                "NULL AS 2015_murders,\n" +
                "NULL AS 2016_murders,\n" +
                "NULL AS change_2014_2015,\n" +
                "NULL AS change_2015_2016,\n" +
                "SOURCE_2.hate_crimes_per_100k_splc_SOURCE_2 AS hate_crimes,\n" +
                "SOURCE_2.avg_hatecrimes_per_100k_fbi_SOURCE_2 AS avg_hatecrimes\n" +
                "FROM SOURCE_2\n" +
                "\n" +
                "UNION ALL\n" +
                "\n" +
                "SELECT\n" +
                "\n" +
                "SOURCE_5.city_SOURCE_5 AS city,\n" +
                "SOURCE_5.state_SOURCE_5 AS state,\n" +
                "SOURCE_5.2014_murders_SOURCE_5 AS 2014_murders,\n" +
                "GREATEST(SOURCE_5.2015_murders_SOURCE_5,SOURCE_5N2.2015_murders_SOURCE_5N2) AS 2015_murders, \n" +
                "SOURCE_5N2.2016_murders_SOURCE_5N2 AS 2016_murders,\n" +
                "SOURCE_5.change_SOURCE_5 AS change_2014_2015,\n" +
                "SOURCE_5N2.change_SOURCE_5N2 AS change_2015_2016,\n" +
                "NULL AS hate_crimes,\n" +
                "NULL AS avg_hatecrimes\n" +
                "FROM SOURCE_5, SOURCE_5N2\n" +
                "WHERE SOURCE_5.state_SOURCE_5 = SOURCE_5N2.state_SOURCE_5N2\n" +
                "AND SOURCE_5.city_SOURCE_5 = SOURCE_5N2.city_SOURCE_5N2");
    }
}
