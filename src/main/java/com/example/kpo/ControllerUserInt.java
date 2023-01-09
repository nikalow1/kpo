package com.example.kpo;

import com.example.kpo.DataBase.Const;
import com.example.kpo.DataBase.DataBaseHandler;
import com.example.kpo.DataType.*;
import com.example.kpo.Function.ControllerConfirmation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;


public class ControllerUserInt {

    @FXML
    private Button back2;

    @FXML
    private TableColumn<Data, String> brandCol;;

    @FXML
    private TableColumn<Data, Integer> countCol;

    @FXML
    private TableView<Data> dataTable;

    @FXML
    private TableColumn<Data, Date> dateOfAdmissionCol;

    @FXML
    private TableColumn<Data, Date> dateOfIssueCol;

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
    private Button refreshData;

    @FXML
    private TableColumn<Data, Integer> statusCol;
    ObservableList<Data> DataList = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        refresh();
        loadDate();
        refreshData.setOnAction(event -> {
            refresh();
        });
        profitAdm.setOnAction(event -> {
            getAddView("dataProfit.fxml");
        });
        outstandOrderAdm.setOnAction(event -> {
            outstandOrder();
            loadDate();
        });

        back2.setOnAction(event -> {
            if(ControllerConfirmation.AlertWarning()) {
                back2.getScene().getWindow().hide();
                openNewScene("hello-view.fxml");
            }
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
            DataList.clear();
            String query = "SELECT * FROM " + Const.DATA_TABLE;
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
    private void getAddView(String window){
        try {
            Parent parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(window)));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException e) {
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