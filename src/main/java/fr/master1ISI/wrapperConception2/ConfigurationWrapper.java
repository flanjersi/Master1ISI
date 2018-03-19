package fr.master1ISI.wrapperConception2;

import java.io.File;

public class ConfigurationWrapper {

    private File file;
    private char separator = ',';
    private boolean firstLineIsDeclarationAttr = true;

    private String nameTable;

    public ConfigurationWrapper(File file){
        this.file = file;
        this.nameTable = file.getName().substring(0, file.getName().indexOf('.'));
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
}
