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
    public CTask (ProcessManager manager,Activity activity,Task task){
        this.manager = manager;
        this.activity = activity;
        this.task = task;
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

        // Botones de Acción
        HBox actionButtons = new HBox(10);
        Button completeButton = new Button("Complete");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");
        completeButton.getStyleClass().add("task-button");
        editButton.getStyleClass().addAll("task-button", "task-button-edit");
        deleteButton.getStyleClass().addAll("task-button", "task-button-delete");
        // Agregar estilos y eventos a los botones aquí
        actionButtons.getChildren().addAll(completeButton, editButton, deleteButton);

        taskCard.getChildren().addAll(titleLabel, statusLabel, actionButtons);

        return taskCard;
    }

    private void handleCloseAction(VBox card) {
        if (card.getParent() != null) {
           //  CustomDoublyLinkedList<Activity> activities = process.getActivities();
          //  activities.removeIf(at -> at.equals(activity));
            ((Pane) card.getParent()).getChildren().remove(card);
           // manager.saveData();
        }
    }
}
