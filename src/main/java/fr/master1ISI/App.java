package fr.master1ISI;

import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.mediator.Mediator;

import java.util.logging.Logger;

public class App {
    public static Logger logger = Logger.getLogger("Logger Wrapper CSV");

    private static DatabaseManager databaseManager;
    private static Mediator mediator;

    private static void initDB() {
        databaseManager = new DatabaseManager("root", null, "ISI");
        databaseManager.createViewMurders();
        databaseManager.createViewMurdersStatistics();
    }


    public static void main(String[] args) {
        initDB();

        databaseManager = new DatabaseManager("root", null, "ISI");

        mediator = new Mediator(databaseManager);

        try{
            mediator.sendRequest("SELECT * FROM MURDERS_STATISTICS");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}

