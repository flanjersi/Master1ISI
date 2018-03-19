package fr.master1ISI.wrapperConception1;

import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.wrapperConception1.Wrapper;

import java.io.File;

public class ThreadWrapper extends Thread{

    private Wrapper wrapper;
    private DatabaseManager databaseManager;
    private String dataPathFile;


    public ThreadWrapper(Wrapper wrapper, DatabaseManager databaseManager, String dataPathFile) {
        this.wrapper = wrapper;
        this.databaseManager = databaseManager;
        this.dataPathFile = dataPathFile;
    }

    @Override
    public void run(){
        wrapper.parse(new File(dataPathFile), databaseManager);
    }

}

