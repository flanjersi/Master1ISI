package fr.master1ISI.javafx;

import fr.master1ISI.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class RootApplicationController implements Initializable{

    @FXML
    private Button btnFindMyHome;

    @FXML
    private Button btnSettings;


    @FXML
    private void onMouseClickedBtnFindMyHome(MouseEvent event){
        App.instance.showSearcher();
    }

    @FXML
    private void onMouseClickedBtnSettings(MouseEvent event){
        App.instance.showSettings();
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
