package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import edu.est.process.manager.domain.models.TaskStatus;
import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import edu.est.process.manager.infrastructure.javafx.util.NodeExplorer;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

import java.util.List;
import java.util.function.UnaryOperator;

public class CModal {

    private final ProcessManager manager;
    private final VBox container;
    private boolean createFlag = false;
    private ActionEvent event;
    public CModal(ProcessManager manager, VBox container,ActionEvent e){
        this.manager = manager;
        this.container = container;
        this.event = e;
    }
    public VBox render(boolean process) {
        // Crear el contenedor principal del modal
        VBox modal = new VBox();
        modal.setSpacing(10);
        modal.getStyleClass().add("card");
        modal.setPadding(new Insets(20, 20, 20, 20));

        // Botón de cierre
        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("close-button");
        closeButton.setOnAction(event -> handleCloseAction(modal));
        String titleText = "";

        if(process) {
            titleText = "Activity";
            createFlag = true;
        }else{
            titleText = "Process";
            createFlag = false;
        }

        // Título del modal
        Text title = new Text("Create " + titleText);
        title.getStyleClass().add("card-title");

        // Campo de texto para el nombre
        TextField nameField = new TextField();
        nameField.setPromptText("Nombre del " + titleText);
        nameField.getStyleClass().add("text-modal");

        // Área de texto para la descripción
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Descripción del " + titleText);
        descriptionArea.getStyleClass().add("text-modal");

        // Botón de acción para crear
        Button createButton = new Button("Crear");
        createButton.getStyleClass().add("button-modal");
        createButton.setOnAction(event -> handleCreateAction(nameField.getText(), descriptionArea.getText(),modal));

        // Organización de componentes
        StackPane topPane = new StackPane(closeButton);
        StackPane.setAlignment(closeButton, javafx.geometry.Pos.TOP_RIGHT);

        modal.getChildren().addAll(topPane, title, nameField, descriptionArea, createButton);

        return modal;
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
        String titleText = "Task";


        // Título del modal
        Text title = new Text("Create " + titleText);
        title.getStyleClass().add("card-title");

        TextField nameField = new TextField();
        nameField.setPromptText("Duration min " + titleText);
        nameField.getStyleClass().add("text-modal");


        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]*")) { // Permite solo números
                return change;
            }
            return null; // Rechaza el cambio si no son números
        };


        TextFormatter<Number> textFormatter = new TextFormatter<>(new NumberStringConverter(), null, filter);
        nameField.setTextFormatter(textFormatter);

        ComboBox<String> statusSelector = new ComboBox<>();
        for (TaskStatus status : TaskStatus.values())  statusSelector.getItems().add(status.toString());

        statusSelector.setValue(TaskStatus.PENDING.toString());

        // Área de texto para la descripción
        TextArea descriptionArea = new TextArea();
        descriptionArea.setPromptText("Descripción del " + titleText);
        descriptionArea.getStyleClass().add("text-modal");

        // Botón de acción para crear
        Button createButton = new Button("Crear");
        createButton.getStyleClass().add("button-modal");
        // createButton.setOnAction(event -> handleCreateAction(nameField.getText(), descriptionArea.getText(),modal));

        // Organización de componentes
        StackPane topPane = new StackPane(closeButton);
        StackPane.setAlignment(closeButton, javafx.geometry.Pos.TOP_RIGHT);

        modal.getChildren().addAll(topPane, title,nameField,statusSelector, descriptionArea, createButton);

        return modal;
    }

    // Métodos de manejo de acciones
    private void handleCloseAction(VBox modal) {
        if (modal.getParent() != null) {
            ((Pane) modal.getParent()).getChildren().remove(modal);
        }
    }

    private void handleCreateAction(String name, String description,VBox modal) {
        String nameProcess = name.replaceAll("\\s+", " ");
        String descriptionProcess = description.replaceAll("\\s+", " ");

        if(nameProcess.trim().isEmpty()) return;
        if(descriptionProcess.trim().isEmpty()) return;

        if(createFlag){
            String id = container.getId();
            Activity activity = new Activity(name,description);
            CustomProcess process = manager.getProcess(id);
            CustomDoublyLinkedList<Activity> verify = process.getActivities();

            Activity aux = verify.findFirst(act -> act.getName().equals(name));
            if (aux != null) return;


            Node root = (Node) event.getSource();
            List<Node> nodesWithId = NodeExplorer.findNodes(root.getParent(), node -> id.equals(node.getId()));
            if (nodesWithId.get(0) instanceof VBox){
                VBox containerActivity = (VBox) nodesWithId.get(0);

                process.addActivity(activity);
                CActivity renderActivity = new CActivity(process,activity,manager);
                containerActivity.getChildren().add(renderActivity.render());
            }
        }else{
            CustomProcess newProcess = new CustomProcess(nameProcess,description);
            manager.addProcess(newProcess);
            CProcess renderProcess = new CProcess(newProcess,manager);
            renderProcess.setContainer(container);
            container.getChildren().add(renderProcess.render());

        }
        handleCloseAction(modal);
       manager.saveData();
    }

    private void handleCreateTaskAction(String name, String description,VBox modal) {
        /*
        String nameProcess = name.replaceAll("\\s+", " ");
        String descriptionProcess = description.replaceAll("\\s+", " ");

        if(nameProcess.trim().isEmpty()) return;
        if(descriptionProcess.trim().isEmpty()) return;

        if(createFlag){
            String id = container.getId();
            Activity activity = new Activity(name,description);
            CustomProcess process = manager.getProcess(id);
            CustomDoublyLinkedList<Activity> verify = process.getActivities();

            Activity aux = verify.findFirst(act -> act.getName().equals(name));
            if (aux != null) return;


            Node root = (Node) event.getSource();
            List<Node> nodesWithId = NodeExplorer.findNodes(root.getParent(), node -> id.equals(node.getId()));
            if (nodesWithId.get(0) instanceof VBox){
                VBox containerActivity = (VBox) nodesWithId.get(0);

                process.addActivity(activity);
                CActivity renderActivity = new CActivity(process,activity,manager);
                containerActivity.getChildren().add(renderActivity.render());
            }
        }else{
            CustomProcess newProcess = new CustomProcess(nameProcess,description);
            manager.addProcess(newProcess);
            CProcess renderProcess = new CProcess(newProcess,manager);
            renderProcess.setContainer(container);
            container.getChildren().add(renderProcess.render());

        }
        handleCloseAction(modal);
        manager.saveData();
        */
    }

    private Node findFirstChildNodeWithIdAndWithoutClass(String nodeId, String excludeClass) {
        Node sourceNode = (Node) event.getSource();
        Parent parent = sourceNode.getParent();

        if (parent != null) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                if (nodeId.equals(child.getId()) && !child.getStyleClass().contains(excludeClass)) {
                    return child; // Retorna el primer nodo que cumple con los criterios
                }
            }
        }

        return null; // No se encontró ningún nodo que cumpla con los criterios
    }
}
