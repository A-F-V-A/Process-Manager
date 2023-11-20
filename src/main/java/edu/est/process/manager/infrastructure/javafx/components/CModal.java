package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.*;
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
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
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

        StringConverter<String> simpleConverter = new DefaultStringConverter();
        TextFormatter<String> textFormatter = new TextFormatter<>(simpleConverter, "", filter);
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
        createButton.setOnAction(event -> handleCreateTaskAction(nameField.getText(), descriptionArea.getText(),modal,statusSelector.getValue()));

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

    private void handleCreateTaskAction(String time, String description,VBox modal, String state) {
        String nameProcess = time.replaceAll("\\s+", " ");
        String descriptionProcess = description.replaceAll("\\s+", " ");

        if(nameProcess.trim().isEmpty()) return;
        if(descriptionProcess.trim().isEmpty()) return;

        String id = container.getId();
        CustomProcess process = manager.getProcess(id);
        List<Node> nodeFilter = NodeExplorer.findNodes(modal.getParent(), node -> node.getStyleClass().contains("task-view"));
        if (nodeFilter.size() == 1 && nodeFilter.get(0) instanceof VBox) {
            VBox vBoxNode = (VBox) nodeFilter.get(0);
            Activity activity =  process.getActivities().findFirst(act -> act.getId().equals(vBoxNode.getId()));
            TaskStatus stateValue = TaskStatus.valueOf(state.toUpperCase());
            Task task = new Task(description,stateValue,Integer.parseInt(time.replaceAll(",","")));
            activity.addTask(task);
            process.updateTotalDuration();
            CTask renderTask = new CTask(manager,activity,task,process.getId());

            nodeFilter = NodeExplorer.findNodes(modal.getParent(), node -> node.getStyleClass().contains("pending-tasks-container"));
            if (nodeFilter.size() == 1 && nodeFilter.get(0) instanceof VBox) {
                VBox completed = (VBox) nodeFilter.get(0);
                completed.getChildren().add(renderTask.render());
            }
            manager.saveData();
        }
        handleCloseAction(modal);
    }
}
