package edu.est.process.manager.infrastructure.javafx.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;

public class CExportImport {
    public VBox render() {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER_LEFT);
        card.setSpacing(10.0);
        card.getStyleClass().add("notification-card");
        card.setPadding(new Insets(10, 10, 10, 10));
        double cardWidth = 300;
        card.setPrefWidth(cardWidth);
        card.setMaxWidth(cardWidth);
        card.setMinWidth(cardWidth);
        card.setPrefHeight(cardWidth);
        card.setMaxWidth(cardWidth);
        card.setMinWidth(cardWidth);

        Text title = new Text("Exportar o Importar Procesos");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Button importButton = new Button("Importar tabla de procesos");
        importButton.setMaxWidth(Double.MAX_VALUE);
        importButton.setOnAction(event -> handleImportAction());

        Button exportButton = new Button("Exportar tabla de procesos");
        exportButton.setMaxWidth(Double.MAX_VALUE);
        exportButton.setOnAction(event -> handleExportAction());

        card.getChildren().addAll(
                title,
                importButton,
                exportButton
        );

        return card;
    }

    private void handleImportAction() {
        fileUpload();
    }

    private void handleExportAction() {

    }

    private void fileUpload() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo Excel");

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Archivos Excel (*.xlsx, *.xls)", "*.xlsx", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("Archivo Excel seleccionado: " + selectedFile.getName());
            // Aquí puedes manejar el archivo seleccionado
            // Por ejemplo, cargarlo o trabajar con él
        } else {
            System.out.println("Ningún archivo Excel seleccionado.");
        }
    }
}
