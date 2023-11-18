package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.ProcessManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CModal {

    private final ProcessManager manager;
    public CModal(ProcessManager manager){
        this.manager = manager;
    }
    public VBox render() {
        // Crear el contenedor principal del modal
        VBox modal = new VBox();
        modal.setSpacing(10);
        modal.getStyleClass().add("card");
        modal.setPadding(new Insets(20, 20, 20, 20));

        // Botón de cierre
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(event -> handleCloseAction(modal));

        // Título del modal
        Text title = new Text("Create Process");
        title.getStyleClass().add("card-title");

        // Campo de texto para el nombre
        TextField nameField = new TextField();
        nameField.setPromptText("Nombre del proceso");
        nameField.getStyleClass().add("text-modal");

        // Área de texto para la descripción
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Descripción del proceso");
        descriptionArea.getStyleClass().add("text-modal");

        // Botón de acción para crear
        Button createButton = new Button("Crear");
        createButton.getStyleClass().add("button-modal");
        createButton.setOnAction(event -> handleCreateAction(nameField.getText(), descriptionArea.getText()));

        // Organización de componentes
        StackPane topPane = new StackPane(closeButton);
        StackPane.setAlignment(closeButton, javafx.geometry.Pos.TOP_RIGHT);

        modal.getChildren().addAll(topPane, title, nameField, descriptionArea, createButton);

        return modal;
    }

    // Métodos de manejo de acciones
    private void handleCloseAction(VBox modal) {
        if (modal.getParent() != null) {
            ((Pane) modal.getParent()).getChildren().remove(modal);
        }
    }

    private void handleCreateAction(String name, String description) {
        // Aquí puedes implementar la lógica para manejar la creación de un nuevo proceso
        System.out.println("Crear proceso: " + name + " - " + description);
    }
}
