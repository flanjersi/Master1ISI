package fr.master1ISI;

import fr.master1ISI.databaseManager.DatabaseManager;
import fr.master1ISI.javafx.RootApplicationController;
import fr.master1ISI.javafx.SearcherController;
import fr.master1ISI.mediator.Mediator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class AppJavaFX extends Application{

    public static Logger logger = Logger.getLogger("Logger Wrapper CSV");

    public static AppJavaFX instance = null;

    private DatabaseManager databaseManager;

    public final int WIDTH_WINDOWS = 900;
    public final int HEIGHT_WINDOWS = 600;

    private Stage primaryStage;
    private Scene currentScene;

    private RootApplicationController rootApplicationController;
    private SearcherController searcherController;

    private Mediator mediator;

    public void showSearcher(){

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/javafx/view/Searcher.fxml"));

        AnchorPane root = null;
        try {
            root = loader.load();
            searcherController = loader.getController();
            changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void showHome(){

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/javafx/view/RootApplication.fxml"));

        AnchorPane root = null;

        try {
            root = loader.load();
            rootApplicationController = loader.getController();
            changeScene(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void changeScene(Parent root){
        this.currentScene = new Scene(root, WIDTH_WINDOWS, HEIGHT_WINDOWS);
        this.primaryStage.setScene(currentScene);
        this.primaryStage.show();
    }



    private void initDB() {
        databaseManager = new DatabaseManager("root", null, "ISI");
        databaseManager.createViewMurders();
        databaseManager.createViewMurdersStatistics();
    }

    @Override
    public void start(Stage primaryStage) {

        initDB();

        databaseManager = new DatabaseManager("root", null, "ISI");

        mediator = new Mediator(databaseManager);

        this.primaryStage = primaryStage;
        instance = this;

        this.primaryStage.setResizable(false);
        this.primaryStage.setTitle("HomeFinder");
        showHome();
    }

    public void changeCursor(Cursor cursor){
        currentScene.setCursor(cursor);
    }

    public static void main(String[] args) {
        launch(args);

    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
