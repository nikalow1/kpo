package com.example.kpo;

import com.example.kpo.Function.ControllerAddData;
import com.example.kpo.Function.ControllerAddUser;
import com.example.kpo.Function.ControllerConfirmation;
import com.example.kpo.DataBase.Const;
import com.example.kpo.DataBase.DataBaseHandler;
import com.example.kpo.DataType.*;
import com.example.kpo.HelloApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;



public class ControllerAdmIntData {
    @FXML
    private Button addData;

    @FXML
    private Button back2;

    @FXML
    private TableColumn<Data, String> brandCol;

    @FXML
    private TableColumn<Data, Integer> countCol;

    @FXML
    private TableColumn<Data, Date> dateOfAdmissionCol;

    @FXML
    private TableColumn<Data, Date> dateOfIssueCol;

    @FXML
    private Button delData;

    @FXML
    private Button editData;

    @FXML
    private Button refreshData;

    @FXML
    private TableColumn<Data, String> fioCol;

    @FXML
    private TableColumn<Data, Integer> idCol;

    @FXML
    private TableColumn<Data, String> nameCol;

    @FXML
    private Button outstandOrderAdm;

    @FXML
    private TableColumn<Data, String> phoneNumCol;

    @FXML
    private Button profitAdm;

    @FXML
    private TableColumn<Data, Integer> statusCol;

    @FXML
    private TableView<Data> dataTable;

    ObservableList<Data> DataList = FXCollections.observableArrayList();
    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Data data = null;


    @FXML
    void initialize(){
        refresh();
        loadDate();
        back2.setOnAction(event -> {
            back2.getScene().getWindow().hide();
            openNewScene("admInterface.fxml");
        });
        addData.setOnAction(event -> {
            getAddView("addData.fxml");
        });
        refreshData.setOnAction(event -> {
            refresh();
        });
        editData.setOnAction(event -> {
            edit();
        });
        delData.setOnAction(event -> {
            if(ControllerConfirmation.AlertWarning()) {
                delData();
            }
        });
        profitAdm.setOnAction(event -> {
            getAddView("dataProfit.fxml");
        });
        outstandOrderAdm.setOnAction(event -> {
            outstandOrder();
            loadDate();
        });
    }
    private void loadDate(){
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        fioCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneNumCol.setCellValueFactory(new PropertyValueFactory<>("numPhone"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        dateOfAdmissionCol.setCellValueFactory(new PropertyValueFactory<>("dateAccept"));
        dateOfIssueCol.setCellValueFactory(new PropertyValueFactory<>("dateIssue"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

    }
    public void refresh(){
        try {
            connection = DataBaseHandler.getDbConnection();
            DataList.clear();
            String query = "SELECT * FROM " + Const.DATA_TABLE;
            PreparedStatement statement = DataBaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                DataList.add(new Data(
                        resultSet.getInt(Const.DATA_ID),
                        resultSet.getString(Const.DATA_NAME),
                        resultSet.getString(Const.DATA_BRAND),
                        resultSet.getString(Const.DATA_FULLNAME),
                        resultSet.getString(Const.DATA_NUMPHONE),
                        resultSet.getInt(Const.DATA_COUNT),
                        resultSet.getDate(Const.DATA_DATEACCEPT),
                        resultSet.getDate(Const.DATA_DATEISSUE),
                        resultSet.getString(Const.DATA_STATUS)));
                dataTable.setItems(DataList);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void getAddView(String window){
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(window));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void delData(){
        try{
            data = dataTable.getSelectionModel().getSelectedItem();
            query = "DELETE FROM "+Const.DATA_TABLE+" WHERE "+Const.DATA_ID+" ="+data.getId();
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
        data = dataTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addData.fxml"));

        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ControllerAddUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        ControllerAddData controllerAddData = loader.getController();
        controllerAddData.setUpdate(true);
        controllerAddData.setTextField(data.getId(), data.getName(), data.getBrand(), data.getFullName(), data.getNumPhone(), data.getCount(), data.getDateAccept(), data.getDateIssue(), data.getStatus());
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    }
    public void outstandOrder(){
        try {
            DataList.clear();
            String query = "SELECT * FROM " + Const.DATA_TABLE + " WHERE "+Const.DATA_STATUS+" = 'Не выполнен' OR "+Const.DATA_DATEISSUE+" = null";
            PreparedStatement statement = DataBaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                DataList.add(new Data(
                        resultSet.getInt("ID"),
                        resultSet.getString("name"),
                        resultSet.getString("brand"),
                        resultSet.getString("fullName"),
                        resultSet.getString("numPhone"),
                        resultSet.getInt("count"),
                        resultSet.getDate("dateAccept"),
                        resultSet.getDate("date"),
                        resultSet.getString("status")));
                dataTable.setItems(DataList);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
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
        stage.show();
    }

}