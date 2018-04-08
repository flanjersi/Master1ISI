package fr.master1ISI.wrapperConception2;

import fr.master1ISI.App;
import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.fileDownloader.FileDownloader;
import fr.master1ISI.xmlParser.Source;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Wrappers {

    private Map<String, Source> wrappers;

    public Wrappers(){
        wrappers = new HashMap<>();
    }

    public void insertWrapper(String nameSrc, FileDownloader fileDownloader, WrapperCSVDynamics wrapperCSV){
        wrappers.put(nameSrc, new Source(nameSrc, wrapperCSV, fileDownloader));
    }

    public void insertWrapper(Source source){
        wrappers.put(source.getNameTable(), source);
    }


    public void refreshData(String nameSrc, Connection connection){
        Source wrapper = wrappers.get(nameSrc);

        FileDownloader fileDownloader = wrapper.getFileDownloader();
        WrapperCSVDynamics wrapperCSVDynamics = wrapper.getWrapperCSVDynamics();

        App.logger.info("Download of the source : " + nameSrc);
        fileDownloader.download();
        App.logger.info("Refresh data into database of the source : " + nameSrc);
        wrapperCSVDynamics.run(connection);
    }

    public void refreshAll(DatabaseManager databaseManager) throws SQLException {
        for(String nameSource : wrappers.keySet()){
            refreshData(nameSource, databaseManager.getConnection());
        }
    }

}
