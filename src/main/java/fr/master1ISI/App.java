package fr.master1ISI;

import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.wrapper.WrapperCSVS3;
import fr.master1ISI.wrapper.WrapperCSVS4;
import fr.master1ISI.wrapper.WrapperCSVS5;
import fr.master1ISI.wrapper.WrapperCSVS5N2;
import java.util.logging.Logger;

public class App {

    public static Logger logger = Logger.getLogger("Logger Wrapper CSV");

    public static void main(String[] args){

        logger.info("INIALISATION DATABASE MANAGER");
        DatabaseManager databaseManager = new DatabaseManager("root", null, "ISI");



        ThreadWrapper threadS5 = new ThreadWrapper(new WrapperCSVS5(), databaseManager,
                "ressources/source5/murder_2015_final.csv");

        ThreadWrapper threadS5N2 = new ThreadWrapper(new WrapperCSVS5N2(), databaseManager,
                "ressources/source5/murder_2016_prelim.csv");

        ThreadWrapper threadS4 = new ThreadWrapper(new WrapperCSVS4(), databaseManager,
                "ressources/source4/clean_data.csv");

        ThreadWrapper threadS3 = new ThreadWrapper(new WrapperCSVS3(), databaseManager,
                "ressources/source3/police_killings.csv");


        try {
            logger.info("PARSE S5");
            threadS5.start();

            logger.info("PARSE S5 2");
            threadS5N2.start();

            logger.info("PARSE S4");
            //threadS4.start();

            logger.info("PARSE S3");
            threadS3.start();

            logger.info("ATTENTE FIN PARSE");

            threadS5.join();
            threadS4.join();
            threadS5N2.join();
            threadS3.join();

            logger.info("FIN PROGRAMME");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
