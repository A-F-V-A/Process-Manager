package edu.est.process.manager.infrastructure.javafx.controllers;


import edu.est.process.manager.infrastructure.javafx.components.CNotification;
import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import edu.est.process.manager.infrastructure.javafx.components.CModal;
import edu.est.process.manager.infrastructure.javafx.components.CProcess;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
        manager = ProcessManager.getInstance();
        manager.loadData();
        ViewProcess();
    }

    @FXML
    public void handleViewProcessClick(ActionEvent event) {
        clear();
        ViewProcess();
    }

    @FXML
    public void handleViewNotificationClick(ActionEvent event) {
        clear();
        Notification();
    }

    @FXML
    public void handleViewImportExportClick(ActionEvent event) {

    }


    private void ViewProcess(){

        VBox container = new VBox(10);
        for (CustomProcess process : manager.getProcesses().values()){
            CProcess renderProcess = new CProcess(process,manager);
            container.getChildren().add(renderProcess.render());
        }

        Button floatingButton = new Button("+");
        floatingButton.getStyleClass().add("floating-button");
        floatingButton.setOnAction(event -> {
            CModal createProcess = new CModal(manager,container);
            VBox modal = createProcess.render();
            modal.setId("modal-process");

            if(nodeExists("modal-process")) return;

            p_container.getChildren().add(modal);
            double xPosition = (p_container.getWidth() - 597.6) / 2;
            modal.setLayoutX(xPosition);
        });


        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
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
        CNotification notification = new CNotification();
        VBox component = notification.render();
        p_container.getChildren().add(component);

        double xPosition = (p_container.getWidth() - 300.0) / 2;
        double yPosition = (p_container.getHeight() - 300.0) / 2;
        component.setLayoutX(xPosition);
        component.setLayoutY(yPosition);
    }
    private boolean nodeExists(String id) {
        for (Node child : p_container.getChildren()) {
            if (id.equals(child.getId())) {
                return true; // the node exist
            }
        }
        return false; // the node not exist
    }
    private void clear(){
        p_container.getChildren().clear();
    }
}
