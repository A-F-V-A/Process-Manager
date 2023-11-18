package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CProcess {
    public String title;
    public String id;
    public String description;
    public int time;

    public ProcessManager manager;
    public CProcess(String title, String id, String description, int time, ProcessManager manager){
        this.title  = title;
        this.id = id;
        this.description = description;
        this.time = time;
        this.manager = manager;
    }
    // Método para renderizar la tarjeta
    public VBox render(){
        VBox card = new VBox();
        card.setSpacing(5.0);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(10, 10, 10, 10));

        // Botón de cierre
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(event -> handleCloseAction(card));

        // Título, descripción y tiempo
        Text cardTitle = new Text(title);
        cardTitle.getStyleClass().add("card-title");

        Text cardDescription = new Text(description);
        cardDescription.getStyleClass().add("card-description");

        Text cardTime = new Text(formatTime());
        cardTime.getStyleClass().add("time-text");

        // Botones de acción
        Button viewButton = new Button("View");
        viewButton.getStyleClass().add("card-button");
        viewButton.setOnAction(event -> handleViewAction());

        Button updateButton = new Button("Update");
        updateButton.getStyleClass().add("card-button");
        updateButton.setOnAction(event -> handleUpdateAction());

        // Organización de componentes
        StackPane topPane = new StackPane(closeButton);
        StackPane.setAlignment(closeButton, javafx.geometry.Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(5, 5, 0, 0));

        HBox buttonsBox = new HBox(5, viewButton, updateButton);
        buttonsBox.setAlignment(javafx.geometry.Pos.CENTER);

        card.getChildren().addAll(topPane, cardTitle, cardDescription, cardTime, buttonsBox);

        return card;
    }

    // Métodos de manejo de acciones
    private void handleCloseAction(VBox card) {
        if (card.getParent() != null) {
            ((Pane) card.getParent()).getChildren().remove(card);
        }
    }

    private void handleViewAction() {
        // Acción para el botón "View"
        System.out.println("View action for " + id);
    }

    private void handleUpdateAction() {
        // Acción para el botón "Update"
        System.out.println("Update action for " + id);
    }

    private String formatTime() {
        int hours = time / 60;
        int minutes = time % 60;

        return String.format("%d:%02d", hours, minutes);
    }

}
