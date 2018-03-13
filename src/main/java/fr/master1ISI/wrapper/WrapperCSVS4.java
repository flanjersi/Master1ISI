package fr.master1ISI.wrapper;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class WrapperCSVS4 extends WrapperCSV {


    public WrapperCSVS4() {
        super(',', true, "SOURCE_4");
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

                String person = nextLine[0];
                String dept = nextLine[1];
                String eow = nextLine[2];
                String cause = nextLine[3];
                String causeShort = nextLine[4];
                String date = nextLine[5];

                int year = Integer.valueOf(nextLine[6]);
                Boolean canine = Boolean.valueOf(nextLine[7]);

                String deptName = nextLine[8];
                String state = nextLine[9];

                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + nameTable + "(person, dept, eow, cause, cause_short, date_murder, year_murder, canine, dept_name, state) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");


                statement.setString(1, person);
                statement.setString(2, dept);
                statement.setString(3, eow);
                statement.setString(4, cause);
                statement.setString(5, causeShort);
                statement.setString(6, date);
                statement.setInt(7, year);
                statement.setBoolean(8, canine);
                statement.setString(9, deptName);
                statement.setString(10, state);

                statement.execute();
                statement.close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void createTable() throws SQLException {
        Statement statement = connection.createStatement();


        statement.execute("CREATE TABLE IF NOT EXISTS " + nameTable + " (" +
                "id_murder int NOT NULL AUTO_INCREMENT," +
                "person VARCHAR (256) NOT NULL , " +
                "dept VARCHAR (256) NOT NULL, " +
                "eow VARCHAR (256) NOT NULL, " +
                "cause VARCHAR (256) NOT NULL, " +
                "cause_short VARCHAR (256) NOT NULL, " +
                "date_murder VARCHAR (256) NOT NULL, " +
                "year_murder int NOT NULL, " +
                "canine BOOLEAN NOT NULL, " +
                "dept_name VARCHAR (256) NOT NULL, " +
                "state VARCHAR (256) NOT NULL, " +
                "CONSTRAINT PK_" + nameTable + " PRIMARY KEY (id_murder) " +
                ") ");

        statement.close();
    }
}
