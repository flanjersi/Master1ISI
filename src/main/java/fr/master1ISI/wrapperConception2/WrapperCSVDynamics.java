package fr.master1ISI.wrapperConception2;

import com.opencsv.CSVReader;
import fr.master1ISI.App;
import javafx.concurrent.Task;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class WrapperCSVDynamics extends Task<Integer>{

    private Connection connection;

    private ConfigurationWrapper cfgWrapper;
    private String[] nameAttrs;

    private CSVReader csvReader;


    public WrapperCSVDynamics(ConfigurationWrapper cfg, Connection connection){
        this.cfgWrapper = cfg;
        this.connection = connection;
    }

    /**
     * Creer la table correspondant à la source CSV
     * @throws SQLException
     */
    private void createTable() throws SQLException{
        Statement statement = connection.createStatement();
        statement.execute(makeRequestTableCreation());
        statement.close();
    }


    /**
     * Parse le fichier csv
     */
    protected Integer call() {
        try {
            csvReader = new CSVReader(new InputStreamReader(new FileInputStream(cfgWrapper.getFile())), ',', '"', '|');
            if(!cfgWrapper.isFirstLineIsDeclarationAttr()) {
                App.logger.warning("ATTENTION, il est impossible de creer la table si la première ligne du fichier n'est pas la déclaration des colonnes");
                App.logger.info("FIN PARSE " + cfgWrapper.getNameTable());
                failed();
                return 0;
            }

            setAttrs();

            App.logger.info("Suppression de la table " + cfgWrapper.getNameTable());
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS " + cfgWrapper.getNameTable());
            statement.close();

            createTable();

            App.logger.log(Level.INFO, "Creation de la table " + cfgWrapper.getNameTable());

            App.logger.log(Level.INFO, "Lecture du fichier csv et insertion des données dans la table " + cfgWrapper.getNameTable());

            readCSVFile();

            connection.close();

            App.logger.log(Level.INFO, "FIN insertion des données dans la table " + cfgWrapper.getNameTable());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            failed();
        } catch (SQLException e) {
            e.printStackTrace();
            failed();
        } catch (IOException e) {
            e.printStackTrace();
            failed();
        }

        updateProgress(cfgWrapper.getNbRowsData(), cfgWrapper.getNbRowsData());
        succeeded();

        return cfgWrapper.getNbRowsData();
    }


    /**
     * Construit la requete d'insertion en fonction de la configuration du fichier csv
     * @param rowDB un ligne du fichier csv
     * @return la requete INSERT SQL
     */
    private String makeRequestInsert(String[] rowDB){

        StringBuilder sb = new StringBuilder("INSERT INTO " + cfgWrapper.getNameTable() + " ");

        sb.append("(");

        for(int index = 0 ; index < nameAttrs.length - 1 ; index++){
            sb.append(nameAttrs[index] + "_" + cfgWrapper.getNameTable() + ",");
        }

        sb.append(nameAttrs[nameAttrs.length - 1] + "_" + cfgWrapper.getNameTable());
        sb.append(")");

        sb.append(" VALUES ");

        sb.append("(");

        for(int index = 0 ; index < rowDB.length - 1 ; index++){
            sb.append("\"" + rowDB[index] + "\",");
        }

        sb.append("\"" + rowDB[rowDB.length - 1] + "\"");
        sb.append(")");

        return sb.toString();
    }

    private String makePreparedStatementRequest(){

        StringBuilder sb = new StringBuilder("INSERT INTO " + cfgWrapper.getNameTable() + " ");

        sb.append("(");

        for(int index = 0 ; index < nameAttrs.length - 1 ; index++){
            sb.append(nameAttrs[index] + "_" + cfgWrapper.getNameTable() + ",");
        }

        sb.append(nameAttrs[nameAttrs.length - 1] + "_" + cfgWrapper.getNameTable());
        sb.append(")");

        sb.append(" VALUES ");

        sb.append("(");

        for(int index = 0 ; index < nameAttrs.length - 1 ; index++){
            sb.append("?,");
        }

        sb.append("?");
        sb.append(")");

        return sb.toString();
    }

    /**
     * Lit le fichier csv ligne par ligne et insert les données dans la base de donnée renseigné
     */
    private void readCSVFile() {
        String[] nextLine;

        int cptRowsData = 0;


        try {
            String preparedRequest = makePreparedStatementRequest();
            PreparedStatement statement = connection.prepareStatement(preparedRequest);

            boolean hasRequestToSend = false;

            while ((nextLine = csvReader.readNext()) != null) {
                int size = nextLine.length;

                if (size == 0)  continue;

                String debut = nextLine[0].trim();

                if (debut.length() == 0 && size == 1) continue;
                if (debut.startsWith("#")) continue;


                for(int index = 0 ; index < nextLine.length ; index++){
                    statement.setString(index + 1, nextLine[index]);
                }

                statement.addBatch();
                hasRequestToSend = true;

                cptRowsData++;

                if(cptRowsData % cfgWrapper.getLimitNbRowsPerBatch() == 0 ){
                    statement.executeLargeBatch();
                    statement.close();
                    statement = connection.prepareStatement(preparedRequest);
                    hasRequestToSend = false;
                    updateProgress(cptRowsData, cfgWrapper.getNbRowsData());
                }
            }

            if(hasRequestToSend){
                statement.executeLargeBatch();
                statement.close();
                updateProgress(cptRowsData, cfgWrapper.getNbRowsData());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Définits les noms des colonnes de la table
     * @throws IOException
     */
    private void setAttrs() throws IOException {
        nameAttrs = csvReader.readNext();
    }

    /**
     * Construit la requête de création de table
     * @return
     */
    private String makeRequestTableCreation(){
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + cfgWrapper.getNameTable() + "(");

        stringBuilder.append("id_" + cfgWrapper.getNameTable() + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY,");

        for(int index = 0 ; index < nameAttrs.length - 1 ; index++){
            stringBuilder.append(nameAttrs[index] + "_" + cfgWrapper.getNameTable() + " TEXT,");
        }

        stringBuilder.append(nameAttrs[nameAttrs.length - 1] + "_" + cfgWrapper.getNameTable() + " TEXT )");

        return stringBuilder.toString();
    }
}
