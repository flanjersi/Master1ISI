package fr.master1ISI.wrapperConception2;

import fr.master1ISI.AppJavaFX;
import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.fileDownloader.FileDownloader;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Wrappers {

    private Map<String, Pair<FileDownloader, WrapperCSVDynamics>> wrappers;

    public Wrappers(){
        wrappers = new HashMap<>();
    }

    public void insertWrapper(String nameSrc, FileDownloader fileDownloader, WrapperCSVDynamics wrapperCSV){
        wrappers.put(nameSrc, new Pair<>(fileDownloader, wrapperCSV));
    }

    public void refreshData(String nameSrc, Connection connection){
        Pair<FileDownloader, WrapperCSVDynamics> wrapper = wrappers.get(nameSrc);

        FileDownloader fileDownloader = wrapper.getKey();
        WrapperCSVDynamics wrapperCSVDynamics = wrapper.getValue();

        AppJavaFX.logger.info("Download of the source : " + nameSrc);
        fileDownloader.download();
        AppJavaFX.logger.info("Refresh data into database of the source : " + nameSrc);
        wrapperCSVDynamics.run(connection);
    }



    public void refreshAll(DatabaseManager databaseManager) throws SQLException {
        for(String nameSource : wrappers.keySet()){
            refreshData(nameSource, databaseManager.getConnection());
        }
    }

}
