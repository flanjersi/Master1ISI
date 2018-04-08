package fr.master1ISI.mediator;

import fr.master1ISI.App;
import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.wrapperConception2.Wrappers;
import fr.master1ISI.xmlParser.ConfigParser;
import fr.master1ISI.xmlParser.Source;
import fr.master1ISI.xmlParser.View;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mediator {

    private HashMap<String, List<String>> viewsToSrc;
    private HashMap<String, String> viewsRequest;

    private DatabaseManager databaseManager;

    private Wrappers wrappers;

    public Mediator(DatabaseManager databaseManager){
        wrappers = new Wrappers();

        this.databaseManager = databaseManager;
        this.viewsToSrc = new HashMap<>();
        this.viewsRequest = new HashMap<>();

        initSource();
        initViews();
    }

    private void initSource(){
        ConfigParser configParser = new ConfigParser("/config.xml");

        try {
            configParser.initParser();

            configParser.parseSources();

            while(configParser.hasNextSource()){
                Source source = configParser.nextSource();

                System.out.println(source.getNameTable());
                wrappers.insertWrapper(source.getNameTable(),
                        source.getFileDownloader(),
                        source.getWrapperCSVDynamics());

            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void initViews(){
        ConfigParser configParser = new ConfigParser("/config.xml");

        try {
            configParser.initParser();

            configParser.parseViews();

            while(configParser.hasNextView()){
                View view = configParser.nextView();

                viewsToSrc.put(view.getName(), view.getTables());
                viewsRequest.put(view.getName(), view.getRequest());
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public boolean sendRequest(String string) throws SQLException {
        Set<String> tables = new HashSet<>();
        List<String> views = parseRequest(string);

        for(String view : views){
            if(!viewsToSrc.containsKey(view.toUpperCase())){
                App.logger.warning("La vue : " + view + " n'existe pas");
                return false;
            }

            List<String> tablesToMakeView = viewsToSrc.get(view);
            tables.addAll(tablesToMakeView);
        }

        StringBuilder sb = new StringBuilder();

        for(String table : tables){
            sb.append(table);
            sb.append(", ");
        }

        sb.deleteCharAt(sb.lastIndexOf(","));

        App.logger.info("Start refresh tables : " + sb.toString());
        refreshAllTables(new ArrayList<>(tables));
        App.logger.info("End refresh tables : " + sb.toString());

        for(String view : views){
            App.getDatabaseManager().sendRequest(viewsRequest.get(view));
        }

        App.logger.info("Send request : " + string);

        App.getDatabaseManager().sendRequestAndPrintResult(string);

        App.logger.info("Request was sent: " + string);

        return true;
    }

    private void refreshAllTables(List<String> tables) throws SQLException {
        for(String table : tables){
            wrappers.refreshData(table, databaseManager.getConnection());
        }
    }

    public void refreshAllSources() throws SQLException {
        wrappers.refreshAll(databaseManager);
    }


    public void refreshAllViews() throws SQLException {
        for(Map.Entry<String, List<String>> entry : viewsToSrc.entrySet()){
            for(String string : entry.getValue()){
                wrappers.refreshData(string, databaseManager.getConnection());
            }
        }
    }


    private List<String> getAllTablesOfRequest(String request){
        try {
            Statement statement = CCJSqlParserUtil.parse(request);
            Select selectStatement = (Select) statement;
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();

            List<String> tmp = tablesNamesFinder.getTableList(selectStatement);

            List<String> tableList = new ArrayList<>();

            for(String table : tmp){
                tableList.add(table.toUpperCase());
            }

            return tableList;
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> parseRequest(String request) {
        return getAllTablesOfRequest(request);
    }

    /**
     * Méthode utilisé dans une version différente du projet
     */
    @Deprecated
    private void addViews(){
        try {
            java.sql.Statement statement = databaseManager.getConnection().createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT TABLE_NAME AS VIEW_NAME, VIEW_DEFINITION\n" +
                    "FROM   INFORMATION_SCHEMA.VIEWS\n" +
                    "WHERE  TABLE_NAME IN\n" +
                    "       (\n" +
                    "        SELECT TABLE_NAME\n" +
                    "        FROM   INFORMATION_SCHEMA.VIEWS\n" +
                    "        WHERE TABLE_SCHEMA = 'ISI'\n" +
                    "        )");


            Set<String> tables = new HashSet<>();
            Pattern pattern = Pattern.compile("(.*)\\.\\`(.*)\\`");

            Matcher matcher;
            while (resultSet.next()){
                String nameView = resultSet.getString(1);
                String requestView = resultSet.getString(2);

                List<String> tablesOfView = getAllTablesOfRequest(requestView);

                for(String table : tablesOfView){
                    matcher = pattern.matcher(table);

                    if(!matcher.matches()) continue;

                    tables.add(matcher.group(2));
                }

                viewsToSrc.put(nameView, new ArrayList<>(tables));
                tables.clear();
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
