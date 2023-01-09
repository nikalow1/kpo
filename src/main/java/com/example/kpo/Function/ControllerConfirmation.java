package com.example.kpo.Function;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class ControllerConfirmation {
    public static boolean AlertWarning() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Предупреждение");
        alert.setHeaderText("Вы действительно хотите сделать это?");
        alert.setContentText("Нажмите ОК для продолжения и Cancel для отмены");

        Optional<ButtonType> option = alert.showAndWait();
        boolean b = false;
        if (option.get() == null || option.get() == ButtonType.CANCEL) {
            b = false;
        }
        if (option.get() == ButtonType.OK) {
            b = true;
        }
        return b;
    }

    public static void AlertError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Неправильно/не введены данные!");
        alert.showAndWait();
    }
}
