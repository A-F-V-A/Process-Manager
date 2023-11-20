package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

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

        Button completeButton = new Button("Complete ✅");
        completeButton.getStyleClass().addAll("task-button","task-button-run");

        Button editButton = new Button("Edit ✎");
        editButton.getStyleClass().addAll("task-button", "task-button-edit");

        Button noticationButtion = new Button("Notify \uD83D\uDD14");
        noticationButtion.getStyleClass().addAll("task-button","task-button-notify");

        Button deleteButton = new Button("Delete 🗑");
        deleteButton.getStyleClass().addAll("task-button", "task-button-delete");
        deleteButton.setOnAction(event -> handleCloseAction(taskCard));


        actionButtons.getChildren().addAll(runButton,completeButton, editButton,noticationButtion, deleteButton);
        taskCard.getChildren().addAll(titleLabel, statusLabel,Time, actionButtons);

        return taskCard;
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

    public void setIdProcess(String idProcess) {
        this.idProcess = idProcess;
    }

    private String formatTime(int time) {
        int hours = time / 60;
        int minutes = time % 60;

        return String.format("%d:%02d", hours, minutes);
    }
}
