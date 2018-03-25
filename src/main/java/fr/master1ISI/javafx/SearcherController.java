package fr.master1ISI.javafx;

import fr.master1ISI.AppJavaFX;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearcherController implements Initializable {

    @FXML
    private ChoiceBox<String> choiceBoxCountry;

    @FXML
    private ChoiceBox<String> choiceBoxState;

    @FXML
    private ChoiceBox<String> choiceBoxCity;

    @FXML
    private ChoiceBox<String> choiceBoxTypeOfDanger;

    @FXML
    private Button btnSubmitResearch1;

    @FXML
    private Button btnSubmitResearch2;

    @FXML
    private Button btnBackHome;

    @FXML
    private ListView<String> listViewResults;

    @FXML
    private Slider sliderDistance;

    private boolean alreadyShowCountries;

    @FXML
    private void onMouseClickedChoiceCountry(MouseEvent event){
        if(alreadyShowCountries) return;

        Task task = new Task() {
            @Override
            protected Void call() throws Exception {

                List<String> countries = AppJavaFX.instance.getDatabaseManager().getAllCountry();
                choiceBoxCountry.getItems().addAll(countries);

                return null;
            }
        };

        task.setOnSucceeded(e -> {
            AppJavaFX.instance.changeCursor(Cursor.DEFAULT); choiceBoxCountry.show();});
        task.setOnFailed(e -> AppJavaFX.instance.changeCursor(Cursor.DEFAULT));

        AppJavaFX.instance.changeCursor(Cursor.WAIT);
        Thread thread = new Thread(task);
        thread.start();

        alreadyShowCountries = true;
    }


    private void initChoiceBoxCountry(){
        choiceBoxCountry.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

            public void changed(ObservableValue<? extends Number> observableValue, Number oldIndex, Number newIndex) {
                String country = choiceBoxCountry.getItems().get(newIndex.intValue());
                List<String> states = AppJavaFX.instance.getDatabaseManager().getAllState(country);

                AppJavaFX.instance.changeCursor(Cursor.WAIT);

                if(states.isEmpty()){
                    choiceBoxState.getItems().add("None");
                    AppJavaFX.instance.changeCursor(Cursor.DEFAULT);
                    return;
                }

                Task task = new Task() {
                    @Override
                    protected Void call() throws Exception {

                        List<String> countries = AppJavaFX.instance.getDatabaseManager().getAllState(country);
                        choiceBoxState.getItems().addAll(countries);

                        return null;
                    }
                };

                task.setOnSucceeded(e -> AppJavaFX.instance.changeCursor(Cursor.DEFAULT));
                task.setOnFailed(e -> AppJavaFX.instance.changeCursor(Cursor.DEFAULT));

                Thread thread = new Thread(task);
                thread.start();

            }
        });
    }

    private void initChoiceBoxState(){

    }

    private void initChoiceBoxTypeOfDanger(){
        choiceBoxTypeOfDanger.getItems().add("None");
        choiceBoxTypeOfDanger.getItems().add("Crimes");
        choiceBoxTypeOfDanger.getItems().add("Terrorism");
        choiceBoxTypeOfDanger.getItems().add("Crimes & Terrorism");
    }

    public void initialize(URL location, ResourceBundle resources) {
        initChoiceBoxCountry();
        initChoiceBoxState();
        initChoiceBoxTypeOfDanger();

        alreadyShowCountries = false;
    }
}
