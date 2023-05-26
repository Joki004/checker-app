package com.start;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StartController {
    @FXML
    private Label welcomeText;
    @FXML
    private Label optionSelected;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    public void startOnePlayerMode(){
        optionSelected.setText("One Player Mode");}
    public void startTwoPlayerMode(){optionSelected.setText("Two Player Mode");}
    public void onlineMode(){optionSelected.setText("Online Mode");}
}