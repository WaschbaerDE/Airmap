package com.ebbrechtair.javafx;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Controller {

    //Anlage der FXML Objekte
    @FXML
    private TextField departmentTextfield;
    @FXML
    private TextField destinationTextfield;
    @FXML
    private Button processButton;
    @FXML
    private TextArea routeTextArea;
    @FXML
    private Button editRouteButton;
    @FXML
    private AnchorPane coordinateAnchorPane;

    //This event get triggered at the moment you press the Button processButton
    //It extracts the input from the 2 input Textfields
    public void initializeProcess(javafx.event.ActionEvent actionEvent) throws InterruptedException {
        String departureAirport = departmentTextfield.getText();
        String destinationAirport = destinationTextfield.getText();
        System.out.println(departureAirport);
        System.out.println(destinationAirport);
    }

    //This event get triggered at the moment you press the Button editRouteButton
    //Idea to change this input to a list where you can click on an point to change this point??
        //Much extra work
    public void editRoute(javafx.event.ActionEvent actionEvent) throws InterruptedException {
        String routeString = routeTextArea.getText();
        String[] routeArray = routeString.split(" ");

        for (int i = 0; i < routeArray.length; i++)
            System.out.println(routeArray[i]);
    }
}
