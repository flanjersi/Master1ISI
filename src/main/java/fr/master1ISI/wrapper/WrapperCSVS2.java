package fr.master1ISI.wrapper;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WrapperCSVS2 extends WrapperCSV {

    public WrapperCSVS2() {
        super(',', true, "SOURCE_2");
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

                String state = getString(nextLine[0]);
                Double median_household_income = getDouble(nextLine[1]);
                Double share_unemployed_seasonal = getDouble(nextLine[2]);
                Double share_population_in_metro_areas = getDouble(nextLine[3]);
                Double share_population_with_high_school_degree = getDouble(nextLine[4]);
                Double share_non_citizen = getDouble(nextLine[5]);
                Double share_white_poverty = getDouble(nextLine[6]);
                Double gini_index = getDouble(nextLine[7]);
                Double share_non_white = getDouble(nextLine[8]);
                Double share_voters_voted_trump = getDouble(nextLine[9]);
                Double hate_crimes_per_100k_splc = getDouble(nextLine[10]);
                Double avg_hatecrimes_per_100k_fbi = getDouble(nextLine[11]);

                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + nameTable + " (state, median_household_income, share_unemployed_seasonal, share_population_in_metro_areas, share_population_with_high_school_degree, share_non_citizen, share_white_poverty, gini_index, share_non_white, share_voters_voted_trump, hate_crimes_per_100k_splc, avg_hatecrimes_per_100k_fbi) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

                statement.setString(1, state);
                verifyDouble(statement,2, median_household_income);
                verifyDouble(statement,3, share_unemployed_seasonal);
                verifyDouble(statement,4, share_population_in_metro_areas);
                verifyDouble(statement,5, share_population_with_high_school_degree);
                verifyDouble(statement,6, share_non_citizen);
                verifyDouble(statement,7, share_white_poverty);
                verifyDouble(statement,8, gini_index);
                verifyDouble(statement,9, share_non_white);
                verifyDouble(statement,10, share_voters_voted_trump);
                verifyDouble(statement,11, hate_crimes_per_100k_splc);
                verifyDouble(statement,12, avg_hatecrimes_per_100k_fbi);

                statement.execute();
                statement.close();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getString(String value){
        if(value.length() == 0) return null;
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
                "state                                    VARCHAR(20) NOT NULL PRIMARY KEY,\n" +
                "median_household_income                  INTEGER  NOT NULL,\n" +
                "share_unemployed_seasonal                NUMERIC(5,3) NOT NULL,\n" +
                "share_population_in_metro_areas          NUMERIC(4,2) NOT NULL,\n" +
                "share_population_with_high_school_degree NUMERIC(5,3) NOT NULL,\n" +
                "share_non_citizen                        NUMERIC(4,2),\n" +
                "share_white_poverty                      NUMERIC(4,2) NOT NULL,\n" +
                "gini_index                               NUMERIC(5,3) NOT NULL,\n" +
                "share_non_white                          NUMERIC(4,2) NOT NULL,\n" +
                "share_voters_voted_trump                 NUMERIC(4,2) NOT NULL,\n" +
                "hate_crimes_per_100k_splc                NUMERIC(11,9),\n" +
                "avg_hatecrimes_per_100k_fbi              NUMERIC(11,9)\n" +
                ");");
    }
}
