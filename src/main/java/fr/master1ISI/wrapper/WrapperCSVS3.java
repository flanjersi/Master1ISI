package fr.master1ISI.wrapper;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WrapperCSVS3 extends WrapperCSV {


    public WrapperCSVS3() {
        super(',', true, "SOURCE_3");
    }

    protected void readCSVFile() throws IOException {
        String[] nextLine;

        try {
            while ((nextLine = csvReader.readNext()) != null) {
                int size = nextLine.length;

                if (size == 0)  continue;

                String debut = nextLine[0].trim();

                if (debut.length() == 0 && size == 1) continue;
                if (debut.startsWith("#")) continue;

                String person                = getString(nextLine[0]) ;
                Integer age                  = getInteger(nextLine[1]);
                String  gender               = getString(nextLine[2]) ;
                String  raceethnicity        = getString(nextLine[3]) ;
                String  month_murder         = getString(nextLine[4]) ;
                Integer day_murder           = getInteger(nextLine[5]) ;
                Integer year_murder          = getInteger(nextLine[6]) ;
                String  streetaddress        = getString(nextLine[7]) ;
                String  city                 = getString(nextLine[8]) ;
                String  state                = getString(nextLine[9]) ;
                Double  latitude             = getDouble(nextLine[10]) ;
                Double  longitude            = getDouble(nextLine[11]) ;
                Integer state_fp             = getInteger(nextLine[12]);
                Integer county_fp            = getInteger(nextLine[13]);
                Integer tract_ce             = getInteger(nextLine[14]) ;
                String  geo_id               = getString(nextLine[15]) ;
                Integer county_id            = getInteger(nextLine[16]) ;
                String  namelsad             = getString(nextLine[17]) ;
                String  lawenforcementagency = getString(nextLine[18]) ;
                String  cause                = getString(nextLine[19]) ;
                String  armed                = getString(nextLine[20]);

                Integer pop                  = getInteger(nextLine[21]);
                Double share_white           = getDouble(nextLine[22]);
                Double share_black           = getDouble(nextLine[23]);
                Double share_hispanic        = getDouble(nextLine[24]);

                Double p_income              = getDouble(nextLine[25]) ;
                Double h_income              = getDouble(nextLine[26]) ;
                Integer county_income        = getInteger(nextLine[27]) ;
                Double  comp_income          = getDouble(nextLine[28]) ;
                Integer county_bucket         = getInteger(nextLine[29]);
                Integer nat_bucket           = getInteger(nextLine[30]) ;
                Double  pov                  = getDouble(nextLine[31]) ;
                Double  urate                = getDouble(nextLine[32]) ;
                Double  college              = getDouble(nextLine[33]) ;

                PreparedStatement statement = connection.prepareStatement("INSERT INTO SOURCE_3 (person, age, gender, raceethnicity, month_murder, day_murder, year_murder, streetaddress, city, state, latitude, longitude, state_fp, county_fp, tract_ce, geo_id, county_id, namelsad, lawenforcementagency, cause, armed, pop, share_white, share_black, share_hispanic, p_income, h_income, county_income, comp_income, county_bucket, nat_bucket, pov, urate, college) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                statement.setString(1,  person);

                verifyInteger(statement, age, 2);

                statement.setString(3,  gender);
                statement.setString(4,  raceethnicity);
                statement.setString(5,     month_murder);
                statement.setInt(6,     day_murder );
                statement.setInt(7,     year_murder);
                statement.setString(8,  streetaddress);
                statement.setString(9,  city);
                statement.setString(10, state);
                statement.setDouble(11, latitude);
                statement.setDouble(12, longitude);
                statement.setInt(13,    state_fp);
                statement.setInt(14,    county_fp);
                statement.setInt(15,    tract_ce);
                statement.setString(16,    geo_id);
                statement.setInt(17,    county_id);
                statement.setString(18, namelsad);
                statement.setString(19, lawenforcementagency );
                statement.setString(20, cause);
                statement.setString(21, armed);

                verifyInteger(statement, pop, 22);

                verifyDouble(statement, share_white, 23);
                verifyDouble(statement, share_black, 24);
                verifyDouble(statement, share_hispanic, 25);
                verifyDouble(statement, p_income, 26);
                verifyDouble(statement, h_income, 27);

                verifyInteger(statement, county_income, 28);
                verifyDouble(statement, comp_income, 29);
                verifyInteger(statement, county_bucket, 30);
                verifyInteger(statement, nat_bucket, 31);
                verifyDouble(statement, pov, 32);
                verifyDouble(statement,urate, 33);
                verifyDouble(statement, college, 34);

                statement.execute();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Integer getInteger(String value){

        Integer valueInt;
        try{
            valueInt = Integer.valueOf(value) ;
        } catch (NumberFormatException e){
            valueInt = null;
        }

        return valueInt;
    }


    private String getString(String value){
        if(value.equalsIgnoreCase("Unknown")) return null;
        return value;
    }

    private Double getDouble(String value){

        Double valueDouble;
        try{
            valueDouble = Double.valueOf(value) ;
        } catch (NumberFormatException e){
            valueDouble = null;
        }

        return valueDouble;
    }




    private void verifyInteger(PreparedStatement statement, Integer number, int indexStatement) throws SQLException {
        if(number == null){
            statement.setString(indexStatement, null);
        }
        else{
            statement.setInt(indexStatement, number);
        }
    }

    private void verifyDouble(PreparedStatement statement, Double number, int indexStatement) throws SQLException {
        if(number == null){
            statement.setString(indexStatement, null);
        }
        else{
            statement.setDouble(indexStatement, number);
        }
    }

    protected void createTable() throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS " + nameTable + "(" +
                "id_murder INT NOT NULL AUTO_INCREMENT,\n" +
                "person               VARCHAR(33) ,\n" +
                "age                  INT,\n" +
                "gender               VARCHAR(6) ,\n" +
                "raceethnicity        VARCHAR(22) ,\n" +
                "month_murder         VARCHAR(8) ,\n" +
                "day_murder           INTEGER  ,\n" +
                "year_murder          INTEGER  ,\n" +
                "streetaddress        VARCHAR(65) ,\n" +
                "city                 VARCHAR(22) ,\n" +
                "state                VARCHAR(2) ,\n" +
                "latitude             NUMERIC(10,7) ,\n" +
                "longitude            INTEGER  ,\n" +
                "state_fp             INTEGER  ,\n" +
                "county_fp            INTEGER,\n" +
                "tract_ce             INTEGER,\n" +
                "geo_id               VARCHAR (20),\n" +
                "county_id            INTEGER,\n" +
                "namelsad             VARCHAR(20),\n" +
                "lawenforcementagency VARCHAR(95),\n" +
                "cause                VARCHAR(17),\n" +
                "armed                VARCHAR(18),\n" +
                "pop                  INTEGER  ,\n" +
                "share_white          VARCHAR(4) ,\n" +
                "share_black          VARCHAR(4) ,\n" +
                "share_hispanic       VARCHAR(4) ,\n" +
                "p_income             VARCHAR(20) ,\n" +
                "h_income             VARCHAR(20) ,\n" +
                "county_income        INTEGER  ,\n" +
                "comp_income          VARCHAR(11) ,\n" +
                "county_bucket        INTEGER ,\n" +
                "nat_bucket           VARCHAR(2) ,\n" +
                "pov                  VARCHAR(4) ,\n" +
                "urate                VARCHAR(11) ,\n" +
                "college              VARCHAR(11) ,\n" +
                "CONSTRAINT PK_" + nameTable + " PRIMARY KEY (id_murder) " +
                ");");

        statement.close();
    }
}
