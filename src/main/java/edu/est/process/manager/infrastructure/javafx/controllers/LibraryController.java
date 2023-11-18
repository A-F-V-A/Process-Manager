package edu.est.process.manager.infrastructure.javafx.controllers;

import edu.est.process.manager.infrastructure.javafx.components.CNotification;
import edu.est.process.manager.infrastructure.javafx.components.CProcess;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {


    @FXML
    public AnchorPane p_container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Process();
        Notification();
    }


    private void Process(){
        VBox container = new VBox(10);
        for (int i = 0; i < 10; i++) {
            CProcess process = new CProcess("Task " + i, "ID " + i, "Descripción " + i, 120 + i);
            VBox card = process.render();
            container.getChildren().add(card);
        }

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);

        p_container.getChildren().add(scrollPane);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
    }

    private void Notification() {
        VBox container = new VBox(10);
        CNotification process = new CNotification();
        VBox card = process.render();
        container.getChildren().add(card);


        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        VBox centeringnContainer = new VBox(scrollPane);
        centeringnContainer.setAlignment(Pos.CENTER);

        p_container.getChildren().add(scrollPane);
        AnchorPane.setTopAnchor(scrollPane, 5.0);
        AnchorPane.setRightAnchor(scrollPane, 5.0);
        AnchorPane.setBottomAnchor(scrollPane, 5.0);
        AnchorPane.setLeftAnchor(scrollPane, 5.0);
    }
}
