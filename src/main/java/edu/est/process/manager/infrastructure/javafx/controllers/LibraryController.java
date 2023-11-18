package edu.est.process.manager.infrastructure.javafx.controllers;

import edu.est.process.manager.domain.models.ProcessManager;
import edu.est.process.manager.infrastructure.javafx.components.CProcess;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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


    private void ViewProcess(){



        VBox container = new VBox(10);
        for (int i = 0; i < 10; i++) {
            CProcess process = new CProcess("Task " + i, "ID " + i, "Descripción " + i, 120 + i);
            VBox card = process.render();
            container.getChildren().add(card);
        }

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);


        // Crear botón flotante
        Button floatingButton = new Button("+");
        floatingButton.getStyleClass().add("floating-button"); // Agrega tu clase de estilo si es necesario


        p_container.getChildren().add(scrollPane);
        p_container.getChildren().add(floatingButton);

        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);

        AnchorPane.setBottomAnchor(floatingButton, 10.0); // 10.0 es el margen desde la parte inferior
        AnchorPane.setRightAnchor(floatingButton, 20.0);   // 10.0 es el margen desde la izquierda



    }
}
