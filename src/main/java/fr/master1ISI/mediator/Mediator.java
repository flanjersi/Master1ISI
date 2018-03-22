package fr.master1ISI.mediator;

import fr.master1ISI.App;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Mediator {

    public HashMap<String, List<String>> viewsToSrc;

    public Mediator(){

    }

    public boolean sendRequest(String string){
        Set<String> tables = new HashSet<>();
        String[] views = parseRequest(string);

        for(String view : views){
            if(!viewsToSrc.containsKey(view)){
                App.logger.warning("La vue : " + view + " n'existe pas");
                return false;
            }

            List<String> tablesToMakeView = viewsToSrc.get(view);
            tables.addAll(tablesToMakeView);
        }

        refreshAllTables((String[]) tables.toArray());

        App.instance.databaseManager.sendRequest(string);

        return true;
    }

    private void refreshAllTables(String[] tables) {
        //TODO A FAIRE
    }

    private String[] parseRequest(String string) {
        //TODO A FAIRE
        return null;
    }



    public void addView(String viewName, List<String> tableNames){
        viewsToSrc.put(viewName, tableNames);
    }

}
