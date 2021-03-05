package com.ebbrechtair.javafx;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Controller {

    @FXML
    private TextField depaTextfield;
    @FXML
    private TextField destTextfield;
    @FXML
    private Button processButton;
    @FXML
    private TextArea routeTextArea;
    @FXML
    private Button editRouteButton;
    @FXML
    private AnchorPane coordinateAnchorPane;

    public void initializeProcess(javafx.event.ActionEvent actionEvent) throws InterruptedException {
        String departureAirport = depaTextfield.getText();
        String destinationAirport = destTextfield.getText();
        System.out.println(departureAirport);
        System.out.println(destinationAirport);
    }

    public void editRoute(javafx.event.ActionEvent actionEvent) throws InterruptedException {
        String routeString = routeTextArea.getText();
        String[] routeArray = routeString.split(" ");

        for (int i = 0; i < routeArray.length; i++)
            System.out.println(routeArray[i]);
    }
}
