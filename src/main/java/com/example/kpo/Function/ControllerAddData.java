package com.example.kpo.Function;

import com.example.kpo.DataBase.Const;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class ControllerAddData {
    @FXML
    private TextField addBrand;

    @FXML
    private Button addClear;

    @FXML
    private TextField addCount;

    @FXML
    private TextField addDataAccept;

    @FXML
    private TextField addDateIssue;

    @FXML
    private TextField addFio;

    @FXML
    private TextField addName;

    @FXML
    private TextField addNumPhone;

    @FXML
    private Button addSave;

    @FXML
    private RadioButton addStatus;

    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement;
    private boolean update;
    int dataId;
    @FXML
    void initialize(){
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
        String status = "";
        if(addStatus.isSelected()){
            status = "Выполнен";
        }
        else{
            status = "Не выполнен";
        }
        if (addName.getText().isEmpty() || addBrand.getText().isEmpty() || addFio.getText().isEmpty() || addNumPhone.getText().isEmpty() || addDateIssue.getText().isEmpty()) {
            ControllerConfirmation.AlertError();

        } else {
            getQuery();
            insert(status);
            clean();

        }
    }
    private void getQuery() {
        if (update == false) {
            query = "INSERT INTO "+ Const.DATA_TABLE+" ( "+Const.DATA_NAME+", "+Const.DATA_BRAND+ ",  "+Const.DATA_FULLNAME+",  "+Const.DATA_NUMPHONE+", "+Const.DATA_COUNT+", "+Const.DATA_DATEACCEPT+", "+Const.DATA_DATEISSUE+", "+Const.DATA_STATUS+") VALUES (?,?,?,?,?,?,?,?)";
        }else{
            query = "UPDATE "+Const.DATA_TABLE+" SET "
                    + Const.DATA_NAME+"=?,"
                    + Const.DATA_BRAND+"=?,"
                    + Const.DATA_FULLNAME+"=?,"
                    + Const.DATA_NUMPHONE+"=?,"
                    + Const.DATA_COUNT+"=?,"
                    + Const.DATA_DATEACCEPT+"=?,"
                    + Const.DATA_DATEISSUE+"=?,"
                    + Const.DATA_STATUS+"=? WHERE "+Const.DATA_ID+" = '"+dataId+"'";
        }
    }
    private void insert(String status) {

        try {
            String dateIssue = null;
            if(addDateIssue.getText().isEmpty()){
            }
            else {
                dateIssue = addDateIssue.getText();
            }
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, addName.getText());
            preparedStatement.setString(2, addBrand.getText());
            preparedStatement.setString(3, addFio.getText());
            preparedStatement.setString(4, addNumPhone.getText());
            preparedStatement.setString(5, addCount.getText());
            preparedStatement.setString(6, String.valueOf(addDataAccept.getText()));
            preparedStatement.setString(7, dateIssue);
            preparedStatement.setString(8, status);
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void clean() {
        addName.setText(null);
        addBrand.setText(null);
        addFio.setText(null);
        addNumPhone.setText(null);
        addCount.setText(null);
        addDataAccept.setText(null);
        addDateIssue.setText(null);
    }
    public void setTextField(int id, String name, String brand, String fio, String numPhone, int count, Date dateAccept, Date dateIssue, String status) {
        dataId = id;
        addName.setText(name);
        addBrand.setText(brand);
        addFio.setText(fio);
        addNumPhone.setText(numPhone);
        addCount.setText(String.valueOf(count));
        addDataAccept.setText(String.valueOf(dateAccept));
        addDateIssue.setText(String.valueOf(dateIssue));
        addStatus.setText(status);
    }
    public void setUpdate(boolean b) {
        this.update = b;
    }
}
