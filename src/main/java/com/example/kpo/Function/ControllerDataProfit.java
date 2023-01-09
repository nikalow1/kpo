package com.example.kpo.Function;

import com.example.kpo.DataBase.Const;
import com.example.kpo.DataBase.DataBaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.kpo.Function.ControllerConfirmation.AlertError;

public class ControllerDataProfit {

    @FXML
    private Button clear;

    @FXML
    private TextField dateC;

    @FXML
    private TextField datePo;

    @FXML
    private Button result;

    @FXML
    void initialize() {
        result.setOnAction(event -> {
            try {
                profit();
                clean();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        clear.setOnAction(event -> {
            clean();
        });
    }

    private void profit() throws SQLException, ClassNotFoundException {
        String date1 = dateC.getText();
        String date2 = datePo.getText();
        if (dateC.getText() == null || datePo.getText() == null || dateC.getText().isEmpty() || datePo.getText().isEmpty()) {
            AlertError();
        } else {
            String query = "SELECT SUM(count) FROM "+ Const.DATA_TABLE+" WHERE "+Const.DATA_STATUS+" = 'Выполнен' AND "+Const.DATA_DATEACCEPT+" BETWEEN '" + date1 + "' AND '" + date2 + "'";
            PreparedStatement statement = DataBaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            int sum = 0;
            if (resultSet.next()) {
                sum = resultSet.getInt(1);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Общий доход за данное время: " + sum);
            alert.showAndWait();
        }
    }
    private void clean(){
        dateC.setText(null);
        datePo.setText(null);
    }

}


