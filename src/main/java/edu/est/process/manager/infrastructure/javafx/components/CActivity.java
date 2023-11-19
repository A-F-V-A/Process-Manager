package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class CActivity {
    private final ProcessManager manager;
    private final CustomProcess process;
    private Activity activity;
    private boolean isEditMode = false;
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

        CheckBox swap =  new CheckBox();


        // Título, descripción y tiempo
        Text cardTitle = new Text(activity.getName());
        cardTitle.getStyleClass().add("card-title");

        Text cardDescription = new Text(activity.getDescription());
        cardDescription.getStyleClass().add("card-description");

        // Botones de acción
        Button viewButton = new Button("View Tasks");
        viewButton.getStyleClass().add("card-button");
        // viewButton.setOnAction(event -> handleViewAction());

        Button updateButton = new Button("Update Activity");
        updateButton.getStyleClass().add("card-button");
        updateButton.setOnAction(event -> handleUpdateAction(card,event));

        StackPane topPane = new StackPane();
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setAlignment(upArrow, Pos.TOP_LEFT);
        StackPane.setAlignment(downArrow, Pos.TOP_LEFT);
        StackPane.setAlignment(swap, Pos.TOP_LEFT);

        // Ajustando los márgenes
        StackPane.setMargin(closeButton, new Insets(5, 5, 0, 0));
        StackPane.setMargin(upArrow, new Insets(5, 0, 0, 5));
        StackPane.setMargin(downArrow, new Insets(5, 0, 0, 30)); // Ajusta este valor según sea necesario
        StackPane.setMargin(swap, new Insets(8, 0, 0, 60)); // Ajusta este valor según sea necesario

        // Agregando los botones al StackPane
        topPane.getChildren().addAll(closeButton, upArrow, downArrow,swap);


        HBox buttonsBox = new HBox(5, viewButton, updateButton);
        buttonsBox.setAlignment(javafx.geometry.Pos.CENTER);

        card.getChildren().addAll(topPane, cardTitle, cardDescription, buttonsBox);

        return card;
    }
    private void handleCloseAction(VBox card) {
        if (card.getParent() != null) {
            CustomDoublyLinkedList<Activity> activities = process.getActivities();
            activities.removeIf(at -> at.equals(activity));
            ((Pane) card.getParent()).getChildren().remove(card);
            manager.saveData();
        }
    }
    private void moveCardUp(VBox card) {
        VBox parentContainer = (VBox) card.getParent();
        int currentIndex = parentContainer.getChildren().indexOf(card);
        if (currentIndex > 0) {
            // CustomProcess aux = manager.getProcess(process.getId());
            CustomDoublyLinkedList<Activity> activities = process.getActivities();
            activities.moveNodeBackward(activity);
            activities.forEach(v -> System.out.println(v.toString()),false);
            parentContainer.getChildren().remove(currentIndex);
            parentContainer.getChildren().add(currentIndex - 1, card);

            manager.saveData();
        }
    }
    private void moveCardDown(VBox card) {
        VBox parentContainer = (VBox) card.getParent();
        int currentIndex = parentContainer.getChildren().indexOf(card);

        if (currentIndex < parentContainer.getChildren().size() - 1) {
            //CustomProcess aux = manager.getProcess(process.getId());
            CustomDoublyLinkedList<Activity> activities = process.getActivities();
            activities.moveNodeForward(activity);
            activities.forEach(v -> System.out.println(v.toString()),false);

            parentContainer.getChildren().remove(currentIndex);
            parentContainer.getChildren().add(currentIndex + 1, card);
            manager.saveData();
        }
    }
    private void handleUpdateAction(VBox card, ActionEvent event) {
        if(!isEditMode){
            for (Node node : card.getChildren()) {
                if (node instanceof Text) {
                    Text textNode = (Text) node;
                    if ("card-title".equals(textNode.getStyleClass().get(0))) {
                        // Reemplazar el Text del título por un TextField
                        TextField titleField = new TextField(textNode.getText());
                        titleField.getStyleClass().add("text-modal"); // Agrega tu clase de estilo si es necesario
                        card.getChildren().set(card.getChildren().indexOf(textNode), titleField);
                    } else if ("card-description".equals(textNode.getStyleClass().get(0))) {
                        // Reemplazar el Text de la descripción por un TextArea
                        TextArea descriptionArea = new TextArea(textNode.getText());
                        descriptionArea.getStyleClass().add("area-modal"); // Agrega tu clase de estilo si es necesario
                        card.getChildren().set(card.getChildren().indexOf(textNode), descriptionArea);
                    }
                }
            }
        }else{
            for (Node node : card.getChildren()) {
                if (node instanceof TextField && node.getStyleClass().contains("text-modal")) {
                    TextField titleField = (TextField) node;
                    activity.setName(titleField.getText());
                    Text textTitle = new Text(activity.getName());
                    textTitle.getStyleClass().add("card-title");
                    card.getChildren().set(card.getChildren().indexOf(node), textTitle);
                } else if (node instanceof TextArea && node.getStyleClass().contains("area-modal")) {
                    TextArea descriptionArea = (TextArea) node;
                    activity.setDescription(descriptionArea.getText());
                    Text textDescription = new Text(activity.getDescription());
                    textDescription.getStyleClass().add("card-description");
                    card.getChildren().set(card.getChildren().indexOf(node), textDescription);
                }
            }
            CustomDoublyLinkedList<Activity> activities = process.getActivities();
            Activity uptActivity = activities.findFirst(act -> act.equals(activity));
            uptActivity.setDescription(activity.getDescription());
            uptActivity.setName(activity.getName());
            manager.saveData();
        }
        isEditMode = !isEditMode;

    }
}
