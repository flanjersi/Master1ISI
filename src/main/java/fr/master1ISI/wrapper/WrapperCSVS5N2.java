package fr.master1ISI.wrapper;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WrapperCSVS5N2 extends WrapperCSV {

    public WrapperCSVS5N2() {
        super(',', true, "MURDER_2016_PRELIM");
    }

    protected void createTable() throws SQLException {
        Statement statement = connection.createStatement();

        statement.execute("CREATE TABLE IF NOT EXISTS " + nameTable + " (" +
                "city VARCHAR (256) NOT NULL , " +
                "state VARCHAR (256) NOT NULL, " +
                "2015_murders int NOT NULL, " +
                "2016_murders int NOT NULL, " +
                "difference_murders int NOT NULL, " +
                "CONSTRAINT PK_" + nameTable + " PRIMARY KEY (city) " +
                ") ");

        statement.close();
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

                String city = nextLine[0];
                String state = nextLine[1];
                int murders2015 = Integer.valueOf(nextLine[2]);
                int murders2016 = Integer.valueOf(nextLine[3]);
                int change = Integer.valueOf(nextLine[4]);

                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + nameTable + "(city, state, 2015_murders, 2016_murders, difference_murders) VALUES (?, ?, ?, ?, ?)");
                statement.setString(1, city);
                statement.setString(2, state);
                statement.setInt(3, murders2015);
                statement.setInt(4, murders2016);
                statement.setInt(5, change);

                statement.execute();
                statement.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
