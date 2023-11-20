package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import edu.est.process.manager.infrastructure.javafx.util.NodeExplorer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class CTask {
    private final ProcessManager manager;
    private final Activity activity;
    private Task task;
    private String idProcess;
    public CTask (ProcessManager manager,Activity activity,Task task,String id){
        this.manager = manager;
        this.activity = activity;
        this.task = task;
        this.idProcess = id;
    }

    public VBox render() {
        VBox taskCard = new VBox();
        taskCard.setSpacing(10);
        taskCard.getStyleClass().add("task-card"); // Asegúrate de tener esta clase en tu CSS
        taskCard.setPadding(new Insets(10, 10, 10, 10));

        // Título de la Tarea
        Label titleLabel = new Label(task.getDescription());
        titleLabel.getStyleClass().add("task-title");

        // Estado de la Tarea
        Label statusLabel = new Label(task.getStatus().toString());
        statusLabel.getStyleClass().add("task-status");

        Text Time = new Text(formatTime(task.getDuration()));
        Time.getStyleClass().add("time-text");

        // Botones de Acción
        HBox actionButtons = new HBox(10);

        Button runButton = new Button("run ➤");
        runButton.getStyleClass().add("task-button");
        //runButton.setOnAction(event -> handleNotifyAction(taskCard));

        Button completeButton = new Button("Complete ✅");
        completeButton.getStyleClass().addAll("task-button","task-button-run");
        completeButton.setOnAction(event -> handleCompletedAction(taskCard));

        Button editButton = new Button("Edit ✎");
        editButton.getStyleClass().addAll("task-button", "task-button-edit");

        Button noticationButtion = new Button("Notify \uD83D\uDD14");
        noticationButtion.getStyleClass().addAll("task-button","task-button-notify");
        noticationButtion.setOnAction(event -> handleNotifyAction(taskCard));

        Button deleteButton = new Button("Delete 🗑");
        deleteButton.getStyleClass().addAll("task-button", "task-button-delete");
        deleteButton.setOnAction(event -> handleCloseAction(taskCard));


        actionButtons.getChildren().addAll(runButton,completeButton, editButton,noticationButtion, deleteButton);
        taskCard.getChildren().addAll(titleLabel, statusLabel,Time, actionButtons);

        return taskCard;
    }

    public VBox renderCompleted() {
        VBox taskCard = new VBox();
        taskCard.setSpacing(10);
        taskCard.getStyleClass().add("task-card"); // Asegúrate de tener esta clase en tu CSS
        taskCard.setPadding(new Insets(10, 10, 10, 10));

        // Título de la Tarea
        Label titleLabel = new Label(task.getDescription());
        titleLabel.getStyleClass().add("task-title");

        // Estado de la Tarea
        Label statusLabel = new Label(task.getStatus().toString());
        statusLabel.getStyleClass().add("task-status");

        Text Time = new Text(formatTime(task.getDuration()));
        Time.getStyleClass().add("time-text");

        // Botones de Acción
        HBox actionButtons = new HBox(10);

        Button deleteButton = new Button("Delete 🗑");
        deleteButton.getStyleClass().addAll("task-button", "task-button-delete");
        deleteButton.setOnAction(event -> handleCloseAction(taskCard));


        actionButtons.getChildren().add(deleteButton);
        taskCard.getChildren().addAll(titleLabel, statusLabel,Time, actionButtons);

        return taskCard;
    }

    private void handleNotifyAction(VBox card){
        /*Logica para las notificaciones */
        System.out.println(task.toString());
    }
    private void handleCloseAction(VBox card) {
        if (card.getParent() != null) {
            activity.getPendingTasks().remove(task);
            CustomProcess process = manager.getProcess(idProcess);
            process.updateTotalDuration();
            ((Pane) card.getParent()).getChildren().remove(card);
            manager.saveData();
        }
    }

    private void handleCompletedAction(VBox card) {
        if (card.getParent() != null) {
            task = activity.completeTask(task);

            CTask completeTask = new CTask(manager,activity,task,idProcess);

            Node current = card;
            for (int i = 0; i < 6; i++) {
                if (current != null) current = current.getParent();
                else break;
            }

            List<Node> complete =  NodeExplorer.findNodes(current, node -> node.getStyleClass().contains("completed-tasks-container"));
            if (complete.size() == 1 && complete.get(0) instanceof VBox) {
                VBox completed = (VBox) complete.get(0);
                completed.getChildren().add(completeTask.renderCompleted());
            }
           // ((Pane) card.getParent()).getChildren().remove(card);
            handleCloseAction(card);
            manager.saveData();
        }
    }

    private String formatTime(int time) {
        int hours = time / 60;
        int minutes = time % 60;

        return String.format("%d:%02d", hours, minutes);
    }
}
