package fr.master1ISI.wrapper;

import fr.master1ISI.databaseManager.DatabaseManager;

import java.io.File;

public interface Wrapper {

    /**
     * Parse un fichier dans une base de donnée
     * @param file fichier à parser
     * @param databaseManager
     */
    void parse(File file, DatabaseManager databaseManager);
}
