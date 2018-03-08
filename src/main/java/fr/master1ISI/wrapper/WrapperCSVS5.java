package fr.master1ISI.wrapper;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class WrapperCSVS5 extends WrapperCSV {

    private final String nameTable = "MURDER_2015_FINAL";


    public WrapperCSVS5(char separator) {
        super(separator);
    }


    private void createTable() throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("DROP TABLE " + nameTable);
        statement.execute("CREATE TABLE IF NOT EXISTS " + nameTable + " (" +
                "city VARCHAR NOT NULL," +
                "state VARCHAR NOT NULL," +
                "2014_murders int NOT NULL," +
                "2015_murders int NOT NULL," +
                "change int NOT NULL," +
                "PRIMARY KEY (city)" +
                ")");

        statement.close();

    }

    protected void readCSVFile() throws IOException {
        String[] nextLine;

        try {
            createTable();

            /*while ((nextLine = csvReader.readNext()) != null) {
                int size = nextLine.length;

                if (size == 0)  continue;

                String debut = nextLine[0].trim();

                if (debut.length() == 0 && size == 1) continue;
                if (debut.startsWith("#")) continue;


            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
