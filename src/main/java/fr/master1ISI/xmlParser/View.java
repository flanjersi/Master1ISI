package fr.master1ISI.xmlParser;

import java.util.List;

public class View {

    private String name;
    private String request;
    private List<String> tables;

    public View(String name, String request, List<String> tables) {
        this.name = name;
        this.request = request;
        this.tables = tables;
    }

    public String getName() {
        return name;
    }

    public String getRequest() {
        return request;
    }

    public List<String> getTables() {
        return tables;
    }
}
