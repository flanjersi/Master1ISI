package fr.master1ISI;

import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.mediator.Mediator;

import java.util.Scanner;
import java.util.logging.Level;
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

        //logger.setLevel(Level.OFF);

        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("Saisir une requête sql : ");
            String request = scanner.nextLine();

            try{
                System.out.println("Traitement de la requete en cours");
                mediator.sendRequest(request);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}

