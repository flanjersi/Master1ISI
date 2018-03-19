package fr.master1ISI.javafx;

import fr.master1ISI.App;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private Button btnRefreshDatabase;

    @FXML
    private Button btnBack;

    @FXML
    private ProgressBar progressBarSrc1;


    @FXML
    private ProgressBar progressBarSrc2;


    @FXML
    private ProgressBar progressBarSrc3;



    @FXML
    private ProgressBar progressBarSrc4;


    @FXML
    private ProgressBar progressBarSrc5;


    @FXML
    private ProgressBar progressBarSrc6;

    @FXML
    private ProgressIndicator progressIndicator;


    public SettingsController(){

    }

    @FXML
    private void onMouseClickedBtnRefreshDB(MouseEvent mouseEvent){
        final Task<Integer> taskS1 = App.instance.databaseManager.createAndInsertDataSrc1();
        progressBarSrc1.progressProperty().bind(taskS1.progressProperty());


        final Task<Integer> taskS2 = App.instance.databaseManager.createAndInsertDataSrc2();
        progressBarSrc2.progressProperty().bind(taskS2.progressProperty());



        final Task<Integer> taskS3 = App.instance.databaseManager.createAndInsertDataSrc3();
        progressBarSrc3.progressProperty().bind(taskS3.progressProperty());


        final Task<Integer> taskS4 = App.instance.databaseManager.createAndInsertDataSrc4();
        progressBarSrc4.progressProperty().bind(taskS4.progressProperty());

        final Task<Integer> taskS5 = App.instance.databaseManager.createAndInsertDataSrc5();
        progressBarSrc5.progressProperty().bind(taskS5.progressProperty());

        final Task<Integer> taskS6 = App.instance.databaseManager.createAndInsertDataSrc6();
        progressBarSrc6.progressProperty().bind(taskS6.progressProperty());

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<Thread> threads = new ArrayList<Thread>();
                List<Task> tasks = new ArrayList<Task>();

                tasks.add(taskS1);
                threads.add(new Thread(taskS1));

                tasks.add(taskS2);
                threads.add(new Thread(taskS2));

                tasks.add(taskS3);
                threads.add(new Thread(taskS3));

                tasks.add(taskS4);
                threads.add(new Thread(taskS4));

                tasks.add(taskS5);
                threads.add(new Thread(taskS5));

                tasks.add(taskS6);
                threads.add(new Thread(taskS6));

                for(Thread thread : threads){
                    thread.start();
                }

                updateProgress(0, 1);
                updateProgress(0.5, 1);

                while(!threads.isEmpty()){
                    for(int i = 0 ; i < tasks.size() ; i++){
                        Thread t = threads.get(i);
                        Task task = tasks.get(i);

                        if(!task.isDone()) continue;

                        updateProgress(getProgress() + (1.0/6.0), 1);
                        t.join();
                        threads.remove(i);
                    }
                }

                return null;
            }
        };

        progressIndicator.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    @FXML
    private void onMouseClickedBtnBack(MouseEvent mouseEvent){
        //TODO A faire
        System.out.println("REFRESH");
    }


    public void initialize(URL location, ResourceBundle resources) {
        progressBarSrc1.setProgress(0);
        progressBarSrc2.setProgress(0);
        progressBarSrc3.setProgress(0);
        progressBarSrc4.setProgress(0);
        progressBarSrc5.setProgress(0);
        progressBarSrc6.setProgress(0);
    }
}
