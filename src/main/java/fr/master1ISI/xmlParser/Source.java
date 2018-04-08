package fr.master1ISI.xmlParser;

import fr.master1ISI.fileDownloader.FileDownloader;
import fr.master1ISI.wrapperConception2.WrapperCSVDynamics;

public class Source {

    private String nameTable;
    private WrapperCSVDynamics wrapperCSVDynamics;
    private FileDownloader fileDownloader;

    public Source(String nameTable, WrapperCSVDynamics wrapperCSVDynamics, FileDownloader fileDownloader) {
        this.nameTable = nameTable;
        this.wrapperCSVDynamics = wrapperCSVDynamics;
        this.fileDownloader = fileDownloader;
    }

    public WrapperCSVDynamics getWrapperCSVDynamics() {
        return wrapperCSVDynamics;
    }

    public FileDownloader getFileDownloader() {
        return fileDownloader;
    }


    public String getNameTable() {
        return nameTable;
    }
}
