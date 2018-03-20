package fr.master1ISI.javafx;

import fr.master1ISI.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
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
    private RadioButton choiceBoxSrc1;

    @FXML
    private RadioButton choiceBoxSrc2;

    @FXML
    private RadioButton choiceBoxSrc3;

    @FXML
    private RadioButton choiceBoxSrc4;

    @FXML
    private RadioButton choiceBoxSrc5;

    @FXML
    private RadioButton choiceBoxSrc6;


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
    private ProgressBar progressBarSrc7;


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

        final Task<Integer> taskS7 = App.instance.databaseManager.createAndInsertDataSrc7();
        progressBarSrc7.progressProperty().bind(taskS7.progressProperty());


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<Thread> threads = new ArrayList<Thread>();

                if(choiceBoxSrc1.selectedProperty().get())
                    threads.add(new Thread(taskS1));

                if(choiceBoxSrc2.selectedProperty().get())
                    threads.add(new Thread(taskS2));

                if(choiceBoxSrc3.selectedProperty().get())
                    threads.add(new Thread(taskS3));

                if(choiceBoxSrc4.selectedProperty().get())
                    threads.add(new Thread(taskS4));

                if(choiceBoxSrc5.selectedProperty().get())
                    threads.add(new Thread(taskS5));

                if(choiceBoxSrc6.selectedProperty().get())
                    threads.add(new Thread(taskS6));

                threads.add(new Thread(taskS7));

                for(Thread thread : threads){
                    thread.start();
                }

                updateProgress(0, 1);

                double progress = 0;
                double sizeThread = threads.size();
                while(!threads.isEmpty()){
                    for(int i = 0 ; i < threads.size() ; i++){
                        Thread thread = threads.get(i);

                        if(thread.getState() != Thread.State.TERMINATED) continue;

                        thread.join();
                        progress += (1.0 / sizeThread);

                        updateProgress(progress, 1);
                        threads.remove(i);
                    }
                }

                updateProgress(1,1);

                return null;
            }
        };

        progressIndicator.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    @FXML
    private void onMouseClickedBtnBack(MouseEvent mouseEvent){
        App.instance.showHome();
    }


    private void initProgressBar(){
        progressBarSrc1.setProgress(0);
        progressBarSrc2.setProgress(0);
        progressBarSrc3.setProgress(0);
        progressBarSrc4.setProgress(0);
        progressBarSrc5.setProgress(0);
        progressBarSrc6.setProgress(0);
    }

    private void initRadioButton(){
        choiceBoxSrc1.selectedProperty().setValue(true);
        choiceBoxSrc2.selectedProperty().setValue(true);
        choiceBoxSrc3.selectedProperty().setValue(true);
        choiceBoxSrc4.selectedProperty().setValue(true);
        choiceBoxSrc5.selectedProperty().setValue(true);
        choiceBoxSrc6.selectedProperty().setValue(true);
    }

    public void initialize(URL location, ResourceBundle resources) {
        initProgressBar();
        initRadioButton();
    }
}
