package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import edu.est.process.manager.infrastructure.javafx.util.NodeExplorer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ScrollPaneSkin;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class CProcess {
    public String title;
    public String id;
    public String description;
    public int time;
    private final ProcessManager manager;
    private boolean isEditMode = false;
    private VBox container;
    public CProcess(CustomProcess process, ProcessManager manager){
        this.title  = process.getName();
        this.id = process.getId();
        this.description = process.getDescription();
        this.time = process.getTotalDurationMinutes();
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
        Button viewButton = new Button("View Activity");
        viewButton.getStyleClass().add("card-button");
        viewButton.setOnAction(event -> handleViewAction());

        Button updateButton = new Button("Update Process");
        updateButton.getStyleClass().add("card-button");
        updateButton.setOnAction(event -> handleUpdateAction(card,event));

        // Organización de componentes
        StackPane topPane = new StackPane(closeButton);
        StackPane.setAlignment(closeButton, javafx.geometry.Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(5, 5, 0, 0));

        HBox buttonsBox = new HBox(5, viewButton, updateButton);
        buttonsBox.setAlignment(javafx.geometry.Pos.CENTER);

        card.getChildren().addAll(topPane, cardTitle, cardDescription, cardTime, buttonsBox);

        return card;
    }

    private void handleCloseAction(VBox card) {
        if (card.getParent() != null) {
            manager.removeProcess(id);
            ((Pane) card.getParent()).getChildren().remove(card);
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
                    title = titleField.getText();
                    Text textTitle = new Text(title);
                    textTitle.getStyleClass().add("card-title");
                    card.getChildren().set(card.getChildren().indexOf(node), textTitle);
                } else if (node instanceof TextArea && node.getStyleClass().contains("area-modal")) {
                    TextArea descriptionArea = (TextArea) node;
                    description = descriptionArea.getText();
                    Text textDescription = new Text(description);
                    textDescription.getStyleClass().add("card-description");
                    card.getChildren().set(card.getChildren().indexOf(node), textDescription);
                }
            }
            CustomProcess process = manager.getProcess(id);
            process.setName(title);
            process.setDescription(description);
            manager.saveData();
        }
        isEditMode = !isEditMode;

    }
    private void handleViewAction() {
        container.setVisible(false);
        ScrollPane scrollPane = findScrollPaneParent(container);

        if (scrollPane != null) {
            VBox containerActivity = new VBox(10);
            containerActivity.getStyleClass().add("activity-filter");
            containerActivity.setId(id);
            CustomProcess process = manager.getProcess(id);

            CustomDoublyLinkedList<Activity> activities = process.getActivities();

            activities.forEach(activity ->{
                CActivity renderActivity = new CActivity(process,activity,manager);
                containerActivity.getChildren().add(renderActivity.render());
            },false);

            scrollPane.setContent(containerActivity);
            scrollPane.setFitToWidth(true);
        }
        container.getStyleClass().add("Activity");
        container.setId(id);
        System.out.println(id);
        /*
        Node root = scrollPane.getParent();
        List<Node> nodesWithId = NodeExplorer.findNodes(root, node -> "p_container".equals(node.getId()));
        */
    }

    private ScrollPane findScrollPaneParent(Node node) {
        while (node != null && !(node instanceof ScrollPane)) {
            node = node.getParent();
        }
        return (ScrollPane) node;
    }
    private String formatTime() {
        int hours = time / 60;
        int minutes = time % 60;

        return String.format("%d:%02d", hours, minutes);
    }
    public void setContainer(VBox container) {
        this.container = container;
    }
}
