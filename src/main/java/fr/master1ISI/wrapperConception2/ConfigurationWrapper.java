package fr.master1ISI.wrapperConception2;

import java.io.File;

public class ConfigurationWrapper {

    private File file;
    private char separator = ',';
    private boolean firstLineIsDeclarationAttr = true;
    private int nbRowsData = 0;
    private int limitNbRowsPerBatch = 10_000;

    private String nameTable;

    public ConfigurationWrapper(File file){
        this.file = file;
        this.nameTable = file.getName().substring(0, file.getName().indexOf('.'));
    }

    public ConfigurationWrapper() {
    }

    public void setNameTable(String nameTable) {
        this.nameTable = nameTable;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public void setFirstLineIsDeclarationAttr(boolean firstLineIsDeclarationAttr) {
        this.firstLineIsDeclarationAttr = firstLineIsDeclarationAttr;
    }

    public void setNbRowsData(int nbRowsData) {
        this.nbRowsData = nbRowsData;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public char getSeparator() {
        return separator;
    }

    public boolean isFirstLineIsDeclarationAttr() {
        return firstLineIsDeclarationAttr;
    }

    public String getNameTable() {
        return nameTable;
    }

    public int getNbRowsData() {
        return nbRowsData;
    }


    public int getLimitNbRowsPerBatch() {
        return limitNbRowsPerBatch;
    }

    public void setLimitNbRowsPerBatch(int limitNbRowsPerBatch) {
        this.limitNbRowsPerBatch = limitNbRowsPerBatch;
    }

}
