package fr.master1ISI.mediator;

import fr.master1ISI.App;
import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.fileDownloader.FileDownloader;
import fr.master1ISI.fileDownloader.GitHubFileDownloader;
import fr.master1ISI.fileDownloader.KaggleFileDownloader;
import fr.master1ISI.wrapperConception2.ConfigurationWrapper;
import fr.master1ISI.wrapperConception2.WrapperCSVDynamics;
import fr.master1ISI.wrapperConception2.Wrappers;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mediator {

    private HashMap<String, List<String>> viewsToSrc;

    private DatabaseManager databaseManager;

    private Wrappers wrappers;


    public Mediator(DatabaseManager databaseManager){
        wrappers = new Wrappers();

        this.databaseManager = databaseManager;
        this.viewsToSrc = new HashMap<>();

        initSource();
        addViews();
    }

    private void initSource(){
        FileDownloader downloader;
        ConfigurationWrapper configurationWrapper;
        WrapperCSVDynamics wrapperCSVDynamics;


        /* SOURCE 1 */


        configurationWrapper = new ConfigurationWrapper(new File("dataset/source1/globalterrorismdb_0617dist.csv"));
        configurationWrapper.setNameTable("SOURCE_1");
        configurationWrapper.setNbRowsData(170000);

        downloader = new KaggleFileDownloader("START-UMD/gtd",
                "globalterrorismdb_0617dist.csv",
                "dataset/source1");

        wrapperCSVDynamics = new WrapperCSVDynamics(configurationWrapper);

        wrappers.insertWrapper("SOURCE_1", downloader, wrapperCSVDynamics);


        /* SOURCE 2 */


        configurationWrapper = new ConfigurationWrapper(new File("dataset/source2/hate_crimes.csv"));
        configurationWrapper.setNameTable("SOURCE_2");
        configurationWrapper.setNbRowsData(52);

        wrapperCSVDynamics = new WrapperCSVDynamics(configurationWrapper);
        downloader = new GitHubFileDownloader("fivethirtyeight",
                "data",
                "hate-crimes/hate_crimes.csv",
                "dataset/source2/hate_crimes.csv");

        wrappers.insertWrapper("SOURCE_2", downloader, wrapperCSVDynamics);


        /* SOURCE 3 */


        configurationWrapper = new ConfigurationWrapper(new File("dataset/source3/police_killings.csv"));
        configurationWrapper.setNameTable("SOURCE_3");
        configurationWrapper.setNbRowsData(468);

        wrapperCSVDynamics = new WrapperCSVDynamics(configurationWrapper);
        downloader = new GitHubFileDownloader("fivethirtyeight",
                "data",
                "police-killings/police_killings.csv",
                "dataset/source3/police_killings.csv");

        wrappers.insertWrapper("SOURCE_3", downloader, wrapperCSVDynamics);

        /* SOURCE 4 */

        configurationWrapper = new ConfigurationWrapper(new File("dataset/source4/clean_data.csv"));
        configurationWrapper.setNameTable("SOURCE_4");
        configurationWrapper.setNbRowsData(22801);

        wrapperCSVDynamics = new WrapperCSVDynamics(configurationWrapper);
        downloader = new GitHubFileDownloader("fivethirtyeight",
                "data",
                "police-deaths/clean_data.csv",
                "dataset/source4/clean_data.csv");

        wrappers.insertWrapper("SOURCE_4", downloader, wrapperCSVDynamics);

        /* SOURCE 5 */

        configurationWrapper = new ConfigurationWrapper(new File("dataset/source5/murder_2015_final.csv"));
        configurationWrapper.setNameTable("SOURCE_5");
        configurationWrapper.setNbRowsData(84);

        wrapperCSVDynamics = new WrapperCSVDynamics(configurationWrapper);
        downloader = new GitHubFileDownloader("fivethirtyeight",
                "data",
                "murder_2016/murder_2015_final.csv",
                "dataset/source5/murder_2015_final.csv");

        wrappers.insertWrapper("SOURCE_5", downloader, wrapperCSVDynamics);

        /* SOURCE 5N2 */

        configurationWrapper = new ConfigurationWrapper(new File("dataset/source5/murder_2016_prelim.csv"));
        configurationWrapper.setNameTable("SOURCE_5N2");
        configurationWrapper.setNbRowsData(80);

        wrapperCSVDynamics = new WrapperCSVDynamics(configurationWrapper);
        downloader = new GitHubFileDownloader("fivethirtyeight",
                "data",
                "murder_2016/murder_2016_prelim.csv",
                "dataset/source5/murder_2016_prelim.csv");

        wrappers.insertWrapper("SOURCE_5N2", downloader, wrapperCSVDynamics);

        /* SOURCE 7 */

        configurationWrapper = new ConfigurationWrapper(new File("dataset/source7/US_States.csv"));
        configurationWrapper.setNameTable("US_STATES");
        configurationWrapper.setNbRowsData(52);

        wrapperCSVDynamics = new WrapperCSVDynamics(configurationWrapper);
        downloader = new GitHubFileDownloader("jasonong",
                "List-of-US-States",
                "states.csv",
                "dataset/source7/US_States.csv");

        wrappers.insertWrapper("US_STATES", downloader, wrapperCSVDynamics);

    }


    public boolean sendRequest(String string) throws SQLException {
        Set<String> tables = new HashSet<>();
        List<String> views = parseRequest(string);

        for(String view : views){
            if(!viewsToSrc.containsKey(view)){
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

    private List<String> getAllTablesOfRequest(String request){
        try {
            Statement statement = CCJSqlParserUtil.parse(request);
            Select selectStatement = (Select) statement;
            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> tableList = tablesNamesFinder.getTableList(selectStatement);

            return tableList;
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> parseRequest(String request) {
        return getAllTablesOfRequest(request);
    }

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
