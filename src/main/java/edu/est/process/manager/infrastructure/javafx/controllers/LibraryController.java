package edu.est.process.manager.infrastructure.javafx.controllers;

import edu.est.process.manager.infrastructure.javafx.components.CNotification;
import edu.est.process.manager.domain.models.ProcessManager;
import edu.est.process.manager.infrastructure.javafx.components.CModal;
import edu.est.process.manager.infrastructure.javafx.components.CProcess;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {


    private ProcessManager manager;
    @FXML
    public AnchorPane p_container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Process();
        Notification();
        manager = ProcessManager.getInstance();
        manager.loadData();
        ViewProcess();
    }


    private void ViewProcess(){

        VBox container = new VBox(10);
        for (int i = 0; i < 10; i++) {
            CProcess process = new CProcess(
                    "Task " + i,
                    "ID " + i,
                    "Descripción " + i,
                    120 + i,
                    manager);
            VBox card = process.render();
            container.getChildren().add(card);
        }

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);

        // Crear botón flotante
        Button floatingButton = new Button("+");
        floatingButton.getStyleClass().add("floating-button");
        floatingButton.setOnAction(event -> {
            CModal createProcess = new CModal(manager);
            VBox modal = createProcess.render();
            modal.setId("modal-process");

            if(nodeExists("modal-process")) return;

            p_container.getChildren().add(modal);
            double xPosition = (p_container.getWidth() - 597.6) / 2;
            modal.setLayoutX(xPosition);
        });

        p_container.getChildren().add(scrollPane);
        p_container.getChildren().add(floatingButton);

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);

        AnchorPane.setBottomAnchor(floatingButton, 10.0);
        AnchorPane.setRightAnchor(floatingButton, 20.0);
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
    private boolean nodeExists(String id) {
        for (Node child : p_container.getChildren()) {
            if (id.equals(child.getId())) {
                return true; // the node exist
            }
        }
        return false; // the node not exist
    }
}
