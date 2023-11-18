package edu.est.process.manager.infrastructure.javafx.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CNotification {
    private CheckBox appNotificationCheckbox;
    private CheckBox emailNotificationCheckbox;
    private Label emailLabel;
    private TextField emailField;
    private Label percentageLabel;
    private TextField percentageField;

    public VBox render() {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setSpacing(10.0);
        card.getStyleClass().add("notification-card");
        card.setPadding(new Insets(10, 10, 10, 10));

        Text title = new Text("Cómo deseas ser notificado");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        appNotificationCheckbox = new CheckBox("Notificaciones de aplicación");
        emailNotificationCheckbox = new CheckBox("Notificaciones por correo");

        emailLabel = new Label("Introduzca su correo:");
        emailField = new TextField();
        emailField.setPromptText("Correo electrónico");

        percentageLabel = new Label("Con qué % de tiempo desea ser notificado:");
        percentageField = new TextField();
        percentageField.setPromptText("Ingrese % (solo números)");
        percentageField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                return change;
            }
            return null;
        }));

        emailLabel.setVisible(false);
        emailField.setVisible(false);
        percentageLabel.setVisible(false);
        percentageField.setVisible(false);

        emailNotificationCheckbox.setOnAction(event -> handleEmailCheckboxAction());
        appNotificationCheckbox.setOnAction(event -> handleAppCheckboxAction());

        Button saveButton = new Button("Guardar");
        saveButton.setOnAction(event -> handleSaveAction());

        card.getChildren().addAll(
                title,
                appNotificationCheckbox,
                emailNotificationCheckbox,
                emailLabel,
                emailField,
                percentageLabel,
                percentageField,
                saveButton
        );

        return card;
    }

    private void handleSaveAction() {
        boolean appNotificationSelected = appNotificationCheckbox.isSelected();
        boolean emailNotificationSelected = emailNotificationCheckbox.isSelected();
        String emailAddress = emailField.getText();

        int percentage = 0;
        try {
            percentage = Integer.parseInt(percentageField.getText());
            if (percentage < 0 || percentage > 100) {
                System.out.println("El porcentaje debe estar entre 0 y 100.");
                return; // Salir si el porcentaje no está en el rango válido
            }
            // Aquí podrías guardar el porcentaje en algún lugar o utilizarlo según tu lógica de aplicación
        } catch (NumberFormatException e) {
            System.out.println("Error: El porcentaje debe ser un número entero.");
            return; // Salir si hay un error al parsear el porcentaje
        }

        // Realiza acciones con los valores obtenidos, como almacenarlos en una base de datos, enviarlos a un servidor, etc.
        // Ejemplo: podrías imprimir los valores obtenidos
        System.out.println("Notificaciones de app: " + appNotificationSelected);
        System.out.println("Notificaciones por correo: " + emailNotificationSelected);
        System.out.println("Correo electrónico: " + emailAddress);
        System.out.println("Porcentaje de notificación: " + percentage + "%");

        // Limpiar los campos y deseleccionar los checkboxes
        clearFieldsAndCheckboxes();
    }

    private void clearFieldsAndCheckboxes() {
        appNotificationCheckbox.setSelected(false);
        emailNotificationCheckbox.setSelected(false);
        emailField.clear();
        percentageField.clear();
        emailLabel.setVisible(false);
        emailField.setVisible(false);
        percentageLabel.setVisible(false);
        percentageField.setVisible(false);
    }

    private void handleEmailCheckboxAction() {
        boolean selected = emailNotificationCheckbox.isSelected();
        emailLabel.setVisible(selected);
        emailField.setVisible(selected);
        percentageLabel.setVisible(selected);
        percentageField.setVisible(selected);

        if (selected) {
            appNotificationCheckbox.setSelected(false);
        }
    }

    private void handleAppCheckboxAction() {
        boolean selected = appNotificationCheckbox.isSelected();
        percentageLabel.setVisible(selected);
        percentageField.setVisible(selected);

        if (selected) {
            emailNotificationCheckbox.setSelected(false);
            handleEmailCheckboxAction(); // Para ocultar los campos relacionados a correo
        }
    }
}
