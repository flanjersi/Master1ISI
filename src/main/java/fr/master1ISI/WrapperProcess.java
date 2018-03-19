package fr.master1ISI;

import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.wrapperConception1.*;
import fr.master1ISI.wrapperConception2.ConfigurationWrapper;
import fr.master1ISI.wrapperConception2.WrapperCSVDynamics;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class WrapperProcess {


    public static void lunchC1(){

        Logger logger = App.logger;

        logger.info("INIALISATION DATABASE MANAGER");
        DatabaseManager databaseManager = new DatabaseManager("root", null, "ISI");

        ThreadWrapper threadS5 = new ThreadWrapper(new WrapperCSVS5(), databaseManager,
                "/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/ressources/dataset/source5/murder_2015_final.csv");

        ThreadWrapper threadS5N2 = new ThreadWrapper(new WrapperCSVS5N2(), databaseManager,
                "/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source5/murder_2016_prelim.csv");

        ThreadWrapper threadS4 = new ThreadWrapper(new WrapperCSVS4(), databaseManager,
                "/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source4/clean_data.csv");

        ThreadWrapper threadS3 = new ThreadWrapper(new WrapperCSVS3(), databaseManager,
                "/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source3/police_killings.csv");

        ThreadWrapper threadS2 = new ThreadWrapper(new WrapperCSVS2(), databaseManager,
                "/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source2/hates_crimes.csv");

        ThreadWrapper threadS1 = new ThreadWrapper(new WrapperCSVS1(), databaseManager,
                "/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source1/globalterrorismdb_0617dist.csv");

        try {
            logger.info("PARSE S5");
            threadS5.start();

            logger.info("PARSE S5 2");
            threadS5N2.start();

            logger.info("PARSE S4");
            threadS4.start();

            logger.info("PARSE S3");
            threadS3.start();

            logger.info("PARSE S2");
            threadS2.start();

            logger.info("PARSE S1");
            threadS1.start();


            logger.info("ATTENTE FIN PARSE");

            threadS5.join();
            threadS5N2.join();
            threadS4.join();
            threadS3.join();
            threadS2.join();
            threadS1.join();

            logger.info("FIN PROGRAMME");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void lunchC2(){
       DatabaseManager databaseManager = new DatabaseManager("root", null, "ISI");


        try {
            List<WrapperCSVDynamics> wrappers = new ArrayList<WrapperCSVDynamics>(6);

            ConfigurationWrapper configurationWrapperS1 = new ConfigurationWrapper(new File("/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source1/globalterrorismdb_0617dist.csv"));
            configurationWrapperS1.setNameTable("SOURCE_1");

            wrappers.add(new WrapperCSVDynamics(configurationWrapperS1, databaseManager.getConnection()));

            ConfigurationWrapper configurationWrapperS2 = new ConfigurationWrapper(new File("/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source2/hates_crimes.csv"));
            configurationWrapperS2.setNameTable("SOURCE_2");

            wrappers.add(new WrapperCSVDynamics(configurationWrapperS2, databaseManager.getConnection()));


            ConfigurationWrapper configurationWrapperS3 = new ConfigurationWrapper(new File("/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source3/police_killings.csv"));
            configurationWrapperS3.setNameTable("SOURCE_3");

            wrappers.add(new WrapperCSVDynamics(configurationWrapperS3, databaseManager.getConnection()));

            ConfigurationWrapper configurationWrapperS4 = new ConfigurationWrapper(new File("/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source4/clean_data.csv"));
            configurationWrapperS4.setNameTable("SOURCE_4");

            wrappers.add(new WrapperCSVDynamics(configurationWrapperS4, databaseManager.getConnection()));

            ConfigurationWrapper configurationWrapperS5 = new ConfigurationWrapper(new File("/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source5/murder_2015_final.csv"));
            configurationWrapperS5.setNameTable("SOURCE_5");

            wrappers.add(new WrapperCSVDynamics(configurationWrapperS5, databaseManager.getConnection()));

            ConfigurationWrapper configurationWrapperS5N2 = new ConfigurationWrapper(new File("/home/jeremy/Faculte/Master/S2/ISI/Master1ISI/src/main/ressources/dataset/source5/murder_2016_prelim.csv"));
            configurationWrapperS5N2.setNameTable("SOURCE_5N2");

            wrappers.add(new WrapperCSVDynamics(configurationWrapperS5N2, databaseManager.getConnection()));


            for(WrapperCSVDynamics wrapper : wrappers){
                wrapper.start();
            }

            for(WrapperCSVDynamics wrapper : wrappers){
                wrapper.join();
            }

            System.out.println("FIN INSERTION");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
