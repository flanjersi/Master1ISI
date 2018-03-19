package fr.master1ISI.wrapperConception1;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WrapperCSVS1 extends WrapperCSV{

    public WrapperCSVS1() {
        super(',', true, "SOURCE_1");
    }

    protected void readCSVFile() throws IOException {
        String[] nextLine;

        try {
            int cpt = 0;

            while ((nextLine = csvReader.readNext()) != null) {
                int size = nextLine.length;


                if (size == 0)  continue;

                String debut = nextLine[0].trim();

                if (debut.length() == 0 && size == 1) continue;
                if (debut.startsWith("#")) continue;
                System.out.println(cpt);
                cpt++;
                Integer iyear            = getInteger(nextLine[1]);
                Integer imonth           = getInteger(nextLine[2]);
                Integer iday             = getInteger(nextLine[3]);
                Integer countryCode      = getInteger(nextLine[7]);
                String countryName       = nextLine[8];
                Integer regionCode       = getInteger(nextLine[9]);
                String regionName        = nextLine[10];
                String provState         = nextLine[11];
                String city              = nextLine[12];
                Double latitude          = getDouble(nextLine[13]);
                Double longitude         = getDouble(nextLine[14]);
                Boolean multiple         = Boolean.valueOf(nextLine[25]);
                Boolean succes           = Boolean.valueOf(nextLine[26]);
                Boolean suicide          = Boolean.valueOf(nextLine[27]);
                Integer attackType       = getInteger(nextLine[28]);
                String attackName        = nextLine[29];
                Integer targetType       = getInteger(nextLine[34]);
                String targetName        = nextLine[35];
                Integer targetSubType    = getInteger(nextLine[36]);
                String targetSubName     = nextLine[37];
                String corps             = nextLine[38];
                String target            = nextLine[39];
                Integer natltyCode       = getInteger(nextLine[40]);
                String natltyName        = nextLine[41];
                Integer weaponType       = getInteger(nextLine[81]);
                String weaponName        = nextLine[82];


                PreparedStatement statement = connection.prepareStatement("INSERT INTO SOURCE_1 " +
                        "(iyear, imonth, iday, country_code, country_name, region_code, region_name, prov_state, city, latitude, longitude, multiple, succes, suicide, attack_type_code, attack_type_name, target_type_code, target_type_name, target_type_sub_code, target_type_sub_name, corps, target, natlty_code, natlty_name, weapon_type, weapon_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                verifyInteger(statement,1, iyear);
                verifyInteger(statement,2, imonth);
                verifyInteger(statement,3, iday);
                verifyInteger(statement,4, countryCode);
                statement.setString(5, countryName);
                verifyInteger(statement,6, regionCode);
                statement.setString(7, regionName);
                statement.setString(8, provState);
                statement.setString(9, city);
                verifyDouble(statement,10, latitude);
                verifyDouble(statement,11, longitude);
                statement.setBoolean(12, multiple);
                statement.setBoolean(13, succes);
                statement.setBoolean(14, suicide);
                verifyInteger(statement,15, attackType);
                statement.setString(16, attackName);
                verifyInteger(statement,17, targetType);
                statement.setString(18, targetName);
                verifyInteger(statement,19, targetSubType);
                statement.setString(20, targetSubName);
                statement.setString(21, corps);
                statement.setString(22, target);
                verifyInteger(statement,23, natltyCode);
                statement.setString(24, natltyName);
                verifyInteger(statement,25, weaponType);
                statement.setString(26, weaponName);

                statement.execute();
                statement.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Integer getInteger(String value){

        Integer valueInteger;
        try{
            valueInteger = Integer.valueOf(value) ;
        } catch (NumberFormatException e){
            valueInteger = null;
        }

        return valueInteger;
    }

    private void verifyInteger(PreparedStatement statement, int indexStatement, Integer number) throws SQLException {
        if(number == null){
            statement.setString(indexStatement, null);
        }
        else{
            statement.setInt(indexStatement, number);
        }
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

    private void verifyDouble(PreparedStatement statement, int indexStatement, Double number) throws SQLException {
        if(number == null){
            statement.setString(indexStatement, null);
        }
        else{
            statement.setDouble(indexStatement, number);
        }
    }

    protected void createTable() throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS " + nameTable + "(\n" +
                "id_attack                                INT  NOT NULL AUTO_INCREMENT PRIMARY KEY , " +
                "iyear                                    INTEGER,\n" +
                "imonth                                   INTEGER       ,\n" +
                "iday                                     INTEGER       ,\n" +
                "country_code                             INTEGER       ,\n" +
                "country_name                             VARCHAR(100)  ,\n" +
                "region_code                              INTEGER       ,\n" +
                "region_name                              VARCHAR(100)  ,\n" +
                "prov_state                               VARCHAR(100)  ,\n" +
                "city                                     VARCHAR(100)  ,\n" +
                "latitude                                 NUMERIC(10,7) ,\n" +
                "longitude                                NUMERIC(10,7) ,\n" +
                "multiple                                 BOOLEAN       ,\n" +
                "succes                                   BOOLEAN       ,\n" +
                "suicide                                  BOOLEAN       ,\n" +
                "attack_type_code                         INTEGER       ,\n" +
                "attack_type_name                         VARCHAR(1024)  ,\n" +
                "target_type_code                         INTEGER       ,\n" +
                "target_type_name                         VARCHAR(1024)  ,\n" +
                "target_type_sub_code                     INTEGER       ,\n" +
                "target_type_sub_name                     VARCHAR(1024)  ,\n" +
                "corps                                    VARCHAR(1024)  ,\n" +
                "target                                   VARCHAR(1024)  ,\n" +
                "natlty_code                              INTEGER       ,\n" +
                "natlty_name                              VARCHAR(1024)  ,\n" +
                "weapon_type                              INTEGER       ,\n" +
                "weapon_name                              VARCHAR(1024)  " +
                ");");
    }
}
