package fr.master1ISI;

import java.util.logging.Logger;

public class App{

    public static Logger logger = Logger.getLogger("Logger Wrapper CSV");


    private void refreshDatabase(){
        WrapperProcess.lunchC2();
    }
/*
    @Override
    public void start(Stage primaryStage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("/javafx/view/App.fxml"));

     //   Scene scene = new Scene(root, 900, 600);

        primaryStage.setTitle("HomeFinder");
       // primaryStage.setScene(scene);
        primaryStage.show();
    }*/

    public static void main(String[] args) {
        WrapperProcess.lunchC2();
        //launch(args);
    }
}
