package com.example.kpo.Function;

import com.example.kpo.DataBase.Const;
import com.example.kpo.DataBase.DataBaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ControllerAddUser {

    @FXML
    private Button addClear;

    @FXML
    private TextField addLogin;

    @FXML
    private TextField addPass;

    @FXML
    private TextField addRole;

    @FXML
    private Button addSave;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement;
    private boolean update;
    int userId;

    @FXML
    void initialize() {
        addSave.setOnAction(event -> {
            if(ControllerConfirmation.AlertWarning()){
            try {
                save();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }});
        addClear.setOnAction(event -> {
            clean();
        });
    }
    private void save() throws SQLException, ClassNotFoundException {
        connection = DataBaseHandler.getDbConnection();
        if (addLogin.getText().isEmpty() || addPass.getText().isEmpty() || addRole.getText().isEmpty()) {
            ControllerConfirmation.AlertError();

        } else {
            getQuery();
            insert();
            clean();
        }
    }
    private void getQuery() {

        if (update == false) {

            query = "INSERT INTO "+ Const.USER_TABLE+"( "+Const.USER_USERNAME+", "+Const.USER_PASSWORD+", "+Const.USER_ROLE+") VALUES (?,?,?)";

        }else{
            query = "UPDATE "+Const.USER_TABLE+" SET "
                    + Const.USER_USERNAME+"=?,"
                    + Const.USER_PASSWORD+"=?,"
                    + Const.USER_ROLE + "= ? WHERE "+ Const.USER_ID +"= '"+userId+"'";
        }

    }
    private void insert() {

        try {
            preparedStatement = connection.prepareStatement(query);
            String hashpass = HashPassword.Hash(addPass.getText());
            preparedStatement.setString(1, addLogin.getText());
            preparedStatement.setString(2, hashpass);
            preparedStatement.setInt(3, Integer.parseInt(addRole.getText()));
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void clean() {
        addLogin.setText(null);
        addPass.setText(null);
        addRole.setText(null);
    }
    public void setTextField(int id, String login, String password, int role) {
        userId = id;
        addLogin.setText(login);
        addPass.setText(password);
        addRole.setText(String.valueOf(role));

    }
    public void setUpdate(boolean b) {
        this.update = b;

    }
}
