package edu.est.process.manager.infrastructure.javafx.controllers;


import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.infrastructure.javafx.components.*;
import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import edu.est.process.manager.infrastructure.javafx.util.NodeExplorer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LibraryController implements Initializable {


    private ProcessManager manager;
    @FXML
    public AnchorPane p_container;
    @FXML
    public Button b_process;
    @FXML
    public Button b_notifications;
    @FXML
    public Button b_import_export;
    @FXML
    public TextField searchField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        manager = ProcessManager.getInstance();
        manager.loadData();
        navActive("b_process");
        ViewProcess();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.startsWith(" "))  searchField.setText(newValue);
            else onSearchTextChanged(newValue.trim());
        });
    }

    @FXML
    public void handleViewProcessClick(ActionEvent event) {
        Node source = (Node) event.getSource();
        navActive(source.getId());
        clear();
        ViewProcess();
    }

    @FXML
    public void handleViewNotificationClick(ActionEvent event) {
        Node source = (Node) event.getSource();
        navActive(source.getId());
        clear();
        Notification();
    }

    @FXML
    public void handleViewImportExportClick(ActionEvent event) {
        Node source = (Node) event.getSource();
        navActive(source.getId());
        clear();
        ExportImport();
    }

    private void onSearchTextChanged(String newText) {
        List<Node> nodeFilter = NodeExplorer.findNodes(p_container, node -> node.getStyleClass().contains("activity-filter"));

        if (nodeFilter.size() == 1 && nodeFilter.get(0) instanceof VBox) {
            VBox vBoxNode = (VBox) nodeFilter.get(0);
            CustomProcess process = manager.getProcess(vBoxNode.getId());
            List<Activity> activities = process.getActivities().findAll(act ->
                act.getName().toLowerCase().contains(newText.toLowerCase())
            );
            if(!activities.isEmpty()){
                vBoxNode.getChildren().clear();
                for (Activity act : activities) {
                    CActivity activity = new CActivity(process,act,manager);
                    vBoxNode.getChildren().add(activity.render());
                }
            }

        } else {
            System.out.println("No se encontró exactamente un VBox con la clase 'activity-filter'");
        }

    }


    private void ViewProcess(){

        VBox container = new VBox(10);
        for (CustomProcess process : manager.getProcesses().values()){
            CProcess renderProcess = new CProcess(process,manager);
            container.getChildren().add(renderProcess.render());
            renderProcess.setContainer(container);
        }

        Button floatingButton = new Button("+");
        floatingButton.getStyleClass().add("floating-button");
        floatingButton.setOnAction(event -> {
            CModal createProcess = new CModal(manager,container,event);
            boolean viewActivity =  container.getStyleClass().contains("Activity");
            VBox modal = createProcess.render(viewActivity);
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
    public void ExportImport() {
        VBox container = new VBox(10);
        CExportImport exportImport = new CExportImport();
        VBox component = exportImport.render();
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
    private void navActive(String id){
        String clasName = "nav-active";
        switch (id) {
            case "b_process" -> {
                b_process.getStyleClass().add(clasName);
                b_notifications.getStyleClass().remove(clasName);
                b_import_export.getStyleClass().remove(clasName);
            }
            case "b_notifications" -> {
                b_process.getStyleClass().remove(clasName);
                b_notifications.getStyleClass().add(clasName);
                b_import_export.getStyleClass().remove(clasName);
            }
            case "b_import_export" -> {
                b_process.getStyleClass().remove(clasName);
                b_notifications.getStyleClass().remove(clasName);
                b_import_export.getStyleClass().add(clasName);
            }
            default -> {
                b_process.getStyleClass().remove(clasName);
                b_notifications.getStyleClass().remove(clasName);
                b_import_export.getStyleClass().remove(clasName);
            }
        }
    }
}
