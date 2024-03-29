package fr.master1ISI.wrapperConception1;

import com.opencsv.CSVReader;
import fr.master1ISI.AppJavaFX;
import fr.master1ISI.databaseManager.DatabaseManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public abstract class WrapperCSV implements Wrapper {

    private char separator;
    private boolean firstLineIsDeclarationAttr;

    protected String nameTable;

    protected CSVReader csvReader;
    protected Connection connection;

    public WrapperCSV(char separator, boolean firstLineIsDeclarationAttr, String nameTable){
        this.separator = separator;
        this.firstLineIsDeclarationAttr = firstLineIsDeclarationAttr;
        this.nameTable = nameTable;
    }


    /**
     * Méthode ayant pour but la lecture du fichier csv à partir de la propriété de la classe 'CsvReader'
     * En inserant les données à l'aide de la propriété 'connection'
     */
    protected abstract void readCSVFile() throws IOException;


    protected abstract void createTable() throws SQLException;

    public void parse(File file, DatabaseManager databaseManager) {
        try {
            csvReader = new CSVReader(new FileReader(file));

            if(firstLineIsDeclarationAttr) csvReader.readNext();

            connection = databaseManager.getConnection();


            AppJavaFX.logger.info("Suppression de la table " + nameTable);
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS " + nameTable);
            statement.close();

            AppJavaFX.logger.log(Level.INFO, "Creation de la table " + nameTable);
            createTable();

            AppJavaFX.logger.log(Level.INFO, "Lecture du fichier csv et insertion des données dans la table " + nameTable);
            readCSVFile();

            connection.close();

            AppJavaFX.logger.log(Level.INFO, "FIN insertion des données dans la table " + nameTable);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
