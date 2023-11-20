package edu.est.process.manager.infrastructure.javafx.components;

import javafx.scene.control.Alert;

public class CAlert {

    public static void Alert(Alert.AlertType type, String title, String message, String recommendation){
        Alert alter = new Alert(type);
        alter.setTitle(title);
        alter.setHeaderText(message);
        alter.setContentText(recommendation);
        alter.showAndWait();
    }

}
