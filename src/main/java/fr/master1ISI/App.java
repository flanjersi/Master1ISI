package fr.master1ISI;

import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.wrapper.WrapperCSVS5;

import java.io.File;

public class App {

    public static void main(String[] args){

        DatabaseManager databaseManager = new DatabaseManager("root", " ", "ISI");

        WrapperCSVS5 wrapperS1 = new WrapperCSVS5(',');
        wrapperS1.parse(new File("ressources/source5/murder_2015_final.csv"), databaseManager);
    }
}
