package com.example.kpo;

import com.example.kpo.DataBase.Const;
import com.example.kpo.DataBase.DataBaseHandler;
import com.example.kpo.DataType.*;
import com.example.kpo.Function.ControllerConfirmation;
import com.example.kpo.Function.HashPassword;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.example.kpo.DataBase.DataBaseHandler.getDbConnection;

public class Controller {
    @FXML
    private Button authSignInButton;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;


    @FXML
    void initialize() {
        authSignInButton.setOnAction(event -> {
            String loginText = loginField.getText().trim();
            String loginPassword = passwordField.getText().trim();

            if(!loginText.equals("") && !loginPassword.equals("")){
                loginPassword = HashPassword.Hash(loginPassword);
                loginUser(loginText, loginPassword);
            }
            else
                ControllerConfirmation.AlertError();
        });

    }

    private void loginUser(String loginText, String loginPassword) {
        DataBaseHandler dbHandler = new DataBaseHandler();
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(loginPassword);
        ResultSet result = getUser(user);

        int counter = 0;
        while(true){
            try {
                if (!result.next()) break;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            counter++;
        }
        if(counter >= 1){

            getRole(user);
            if(user.getRole() == 1){
                authSignInButton.getScene().getWindow().hide();
                openNewScene("admInterface.fxml");
            }
            else{
                authSignInButton.getScene().getWindow().hide();
                openNewScene("userInterface.fxml");
            }
        }
        else{
            ControllerConfirmation.AlertError();
        }
    }

    public void openNewScene(String window){
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(window));
        try{
            loader.load();
        } catch (IOException e){
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    public ResultSet getUser(User user){
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USER_USERNAME + "=? AND " + Const.USER_PASSWORD + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getLogin());
            prSt.setString(2, user.getPassword());
            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resSet;
    }

    public void getRole(User user) {
        String query = "select * from "+ Const.USER_TABLE +" where "+Const.USER_USERNAME+"='" + user.getLogin() + "'";
        try{
            Statement statement = getDbConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                user.setRole(resultSet.getInt("role"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
