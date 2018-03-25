package fr.master1ISI.javafx;

import fr.master1ISI.AppJavaFX;
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
    private void onMouseClickedBtnFindMyHome(MouseEvent event){
        AppJavaFX.instance.showSearcher();
    }

    public void initialize(URL location, ResourceBundle resources) {

    }
}
