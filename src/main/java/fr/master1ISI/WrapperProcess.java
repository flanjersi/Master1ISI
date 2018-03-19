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

}
