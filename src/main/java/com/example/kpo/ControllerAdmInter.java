package com.example.kpo;

import com.example.kpo.Function.ControllerConfirmation;
import com.example.kpo.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class ControllerAdmInter {
    @FXML
    private Button accManage;

    @FXML
    private Button exit;

    @FXML
    private Button workData;
    @FXML
    void initialize() {
        accManage.setOnAction(event -> {
            accManage.getScene().getWindow().hide();
            openNewScene("admInterfaceUser.fxml");
        });
        workData.setOnAction(event -> {
            workData.getScene().getWindow().hide();
            openNewScene("admInterfaceData.fxml");
        });
        exit.setOnAction(event -> {
            if(ControllerConfirmation.AlertWarning()) {
                exit.getScene().getWindow().hide();
                openNewScene("hello-view.fxml");
            }
        });
    }
    private void openNewScene(String window){
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(window));
        try{
            loader.load();
        } catch (IOException e){
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
