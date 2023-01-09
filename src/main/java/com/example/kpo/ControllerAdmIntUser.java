package com.example.kpo;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.kpo.Function.ControllerAddUser;
import com.example.kpo.Function.ControllerConfirmation;
import com.example.kpo.DataType.*;
import com.example.kpo.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.example.kpo.DataBase.Const;
import com.example.kpo.DataBase.DataBaseHandler;
import javafx.scene.control.Button;

import static java.util.Objects.requireNonNull;


public class ControllerAdmIntUser {

    @FXML
    private Button addUser;

    @FXML
    private Button backAdm;
    @FXML
    private Button delete;

    @FXML
    private Button edit;

    @FXML
    private TableColumn<User, Integer> idCol;

    @FXML
    private TableColumn<User, String> loginCol;

    @FXML
    private TableColumn<User, String> passCol;

    @FXML
    private Button refresh;

    @FXML
    private TableColumn<User, Integer> roleCol;

    @FXML
    private TableView<User> userTable;

    ObservableList<User> UserList = FXCollections.observableArrayList();
    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    User user = null ;
    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        loadDate();
        backAdm.setOnAction(event -> {
            backAdm.getScene().getWindow().hide();
            openNewScene("admInterface.fxml");
        });
        addUser.setOnAction(event -> {
            getAddView();
        });
        refresh.setOnAction(event -> {
            refresh();
        });
        delete.setOnAction(event -> {
            if(ControllerConfirmation.AlertWarning()) {
                delUser();
            }
        });
        edit.setOnAction(event -> {
            edit();
        });
    }

    private void loadDate() throws SQLException, ClassNotFoundException {
        connection = DataBaseHandler.getDbConnection();
        refresh();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        passCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    public void refresh(){
        try {
            UserList.clear();
            String query = "SELECT * FROM " + Const.USER_TABLE;
            PreparedStatement statement = DataBaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                UserList.add(new User(
                        resultSet.getInt(Const.USER_ID),
                        resultSet.getString(Const.USER_USERNAME),
                        resultSet.getString(Const.USER_PASSWORD),
                        resultSet.getInt(Const.USER_ROLE)));
            userTable.setItems(UserList);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void getAddView(){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("admAddUser.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void delUser(){
        try{
            user = userTable.getSelectionModel().getSelectedItem();
            query = "DELETE FROM "+Const.USER_TABLE+" WHERE "+Const.USER_ID+" ="+user.getId();
            connection = DataBaseHandler.getDbConnection();
            preparedStatement = DataBaseHandler.getDbConnection().prepareStatement(query);
            preparedStatement.execute();
            refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void edit(){
            user = userTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("admAddUser.fxml"));
            try {
                loader.load();
            } catch (IOException ex) {
                Logger.getLogger(ControllerAddUser.class.getName()).log(Level.SEVERE, null, ex);
            }

            ControllerAddUser controllerAddUser = loader.getController();
            controllerAddUser.setUpdate(true);
            controllerAddUser.setTextField(user.getId(), user.getLogin(), user.getPassword(), user.getRole());
            Parent parent = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
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
        stage.show();
    }

}