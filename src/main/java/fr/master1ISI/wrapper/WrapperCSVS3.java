package fr.master1ISI.wrapper;

import java.io.IOException;
import java.sql.SQLException;

public class WrapperCSVS3 extends WrapperCSV {


    public WrapperCSVS3() {
        super(',', true, "SOURCE_3");
    }

    protected void readCSVFile() throws IOException {
        //TODO A Faire
    }

    protected void createTable() throws SQLException {
        //TODO A faire
    }
}
