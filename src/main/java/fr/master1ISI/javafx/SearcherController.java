package fr.master1ISI.javafx;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;

import java.net.URL;
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


    private void initChoiceBoxCountry(){

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
    }
}
