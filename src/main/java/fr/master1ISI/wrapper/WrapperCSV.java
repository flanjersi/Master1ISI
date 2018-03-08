package fr.master1ISI.wrapper;

import au.com.bytecode.opencsv.CSVReader;
import fr.master1ISI.databaseManager.DatabaseManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class WrapperCSV implements Wrapper {

    private char separator;

    protected CSVReader csvReader;
    protected Connection connection;

    public WrapperCSV(char separator){
        this.separator = separator;
    }

    /**
     * Méthode ayant pour but la lecture du fichier csv à partir de la propriété de la classe 'CsvReader'
     * En inserant les données à l'aide de la propriété 'connection'
     */
    protected abstract void readCSVFile() throws IOException;

    public void parse(File file, DatabaseManager databaseManager) {
        try {
            csvReader = new CSVReader(new FileReader(file), separator);
            connection = databaseManager.getConnection();

            readCSVFile();

            connection.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
