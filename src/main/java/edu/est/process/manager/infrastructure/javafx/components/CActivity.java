package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CActivity {
    private final ProcessManager manager;
    private final CustomProcess process;
    private Activity activity;
    public CActivity(CustomProcess process,Activity activity, ProcessManager manager){
        this.manager = manager;
        this.process = process;
        this.activity = activity;
    }
    public VBox render(){
        VBox card = new VBox();
        card.setSpacing(5.0);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(10, 10, 10, 10));

        // Botón de cierre
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(event -> handleCloseAction(card));

        // Botón que cambia el orden
        Button upArrow = new Button("↑");
        upArrow.getStyleClass().add("arrow");
        upArrow.setOnAction(event -> moveCardUp(card));

        // Botón de cierre
        Button downArrow = new Button("↓");
        downArrow.getStyleClass().add("arrow");
        downArrow.setOnAction(event -> moveCardDown(card));


        // Título, descripción y tiempo
        Text cardTitle = new Text(activity.getName());
        cardTitle.getStyleClass().add("card-title");

        Text cardDescription = new Text(activity.getDescription());
        cardDescription.getStyleClass().add("card-description");

        // Botones de acción
        Button viewButton = new Button("View");
        viewButton.getStyleClass().add("card-button");
        // viewButton.setOnAction(event -> handleViewAction());

        Button updateButton = new Button("Update");
        updateButton.getStyleClass().add("card-button");
        // updateButton.setOnAction(event -> handleUpdateAction(card,event));

        StackPane topPane = new StackPane();
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setAlignment(upArrow, Pos.TOP_LEFT);
        StackPane.setAlignment(downArrow, Pos.TOP_LEFT);

        // Ajustando los márgenes
        StackPane.setMargin(closeButton, new Insets(5, 5, 0, 0));
        StackPane.setMargin(upArrow, new Insets(5, 0, 0, 5));
        StackPane.setMargin(downArrow, new Insets(5, 0, 0, 30)); // Ajusta este valor según sea necesario

        // Agregando los botones al StackPane
        topPane.getChildren().addAll(closeButton, upArrow, downArrow);


        HBox buttonsBox = new HBox(5, viewButton, updateButton);
        buttonsBox.setAlignment(javafx.geometry.Pos.CENTER);

        card.getChildren().addAll(topPane, cardTitle, cardDescription, buttonsBox);

        return card;
    }
    private void handleCloseAction(VBox card) {
        if (card.getParent() != null) {
            //manager.removeProcess(id);
            ((Pane) card.getParent()).getChildren().remove(card);
            //manager.saveData();
        }
    }
    private void moveCardUp(VBox card) {
        VBox parentContainer = (VBox) card.getParent();
        int currentIndex = parentContainer.getChildren().indexOf(card);
        if (currentIndex > 0) {
            // Remover la tarjeta y volver a añadirla en la posición anterior
            parentContainer.getChildren().remove(currentIndex);
            parentContainer.getChildren().add(currentIndex - 1, card);
        }
    }
    private void moveCardDown(VBox card) {
        VBox parentContainer = (VBox) card.getParent();
        int currentIndex = parentContainer.getChildren().indexOf(card);
        if (currentIndex < parentContainer.getChildren().size() - 1) {
            // Remover la tarjeta y volver a añadirla en la siguiente posición
            parentContainer.getChildren().remove(currentIndex);
            parentContainer.getChildren().add(currentIndex + 1, card);
        }
    }
}
