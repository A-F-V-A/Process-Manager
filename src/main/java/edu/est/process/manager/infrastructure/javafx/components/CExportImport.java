package edu.est.process.manager.infrastructure.javafx.components;

import edu.est.process.manager.domain.models.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CExportImport {

    private final ProcessManager processManager;

    public CExportImport(ProcessManager processManager) {
        this.processManager = processManager;
    }


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
        CExportImport exportImport = new CExportImport(processManager);
        fileUpload();

    }

    private void handleExportAction() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Procesos");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("ID del Proceso");
        headerRow.createCell(1).setCellValue("Nombre del Proceso");
        headerRow.createCell(2).setCellValue("Descripción");
        headerRow.createCell(3).setCellValue("ID de Actividad");
        headerRow.createCell(4).setCellValue("Nombre de Actividad");
        headerRow.createCell(5).setCellValue("Descripción");
        headerRow.createCell(6).setCellValue("ID de Tarea");
        headerRow.createCell(7).setCellValue("Estado de a Tarea");
        headerRow.createCell(8).setCellValue("Descripción");


        for (CustomProcess process : processManager.getProcesses().values()) {

            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(process.getId());
            row.createCell(1).setCellValue(process.getName());
            row.createCell(2).setCellValue(process.getDescription());

            for (Activity activity : process.getActivities().toList()) {

                row.createCell(3).setCellValue(activity.getId());
                row.createCell(4).setCellValue(activity.getName());
                row.createCell(5).setCellValue(activity.getDescription());

                for (Task task : activity.getPendingTasks().toList()) {

                    row.createCell(6).setCellValue(task.getId());
                    row.createCell(7).setCellValue(task.getStatus().ordinal());
                    row.createCell(8).setCellValue(task.getDescription());
                    row.createCell(9).setCellValue(task.getDurationMinutes());
                }

                for (Task task : activity.getCompletedTasks().toList()) {

                    row.createCell(6).setCellValue(task.getId());
                    row.createCell(7).setCellValue(task.getStatus().ordinal());
                    row.createCell(8).setCellValue(task.getDescription());

                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream("Procesos.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            System.out.println("¡Datos exportados exitosamente a Procesos.xlsx!");
            CAlert.Alert(Alert.AlertType.CONFIRMATION,"Successfully Export","Data has been Export successfully","Closed");

        } catch (IOException e) {
            e.printStackTrace();
            CAlert.Alert(Alert.AlertType.ERROR,"Wrong Export","Data could not be Exported","Closed");
        }
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
            importData(selectedFile);
        } else {
            System.out.println("Ningún archivo Excel seleccionado.");
        }
    }


    public void exportData() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Procesos");

        int rowNum = 0;
        Row headerRow = sheet.createRow(rowNum++);
        headerRow.createCell(0).setCellValue("ID del Proceso");
        headerRow.createCell(1).setCellValue("Nombre del Proceso");
        headerRow.createCell(2).setCellValue("Descripción");
        headerRow.createCell(3).setCellValue("ID de Actividad");
        headerRow.createCell(4).setCellValue("Nombre de Actividad");
        headerRow.createCell(5).setCellValue("Descripción");
//        headerRow.createCell(6).setCellValue("Descripcion");


        for (CustomProcess process : processManager.getProcesses().values()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(process.getId());
            row.createCell(1).setCellValue(process.getName());
            row.createCell(2).setCellValue(process.getDescription());

            for (Activity activity : process.getActivities().toList()) {

                Row row1 = sheet.createRow(rowNum++);
                row.createCell(3).setCellValue(activity.getId());
                row.createCell(4).setCellValue(activity.getName());
                row.createCell(5).setCellValue(activity.getDescription());

            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream("Procesos.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
            System.out.println("¡Datos exportados exitosamente a Procesos.xlsx!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importData(File selectedFile) {
        try {
            FileInputStream inputStream = new FileInputStream(selectedFile);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Puedes cambiar el índice según la hoja que necesites

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                String name = row.getCell(1).getStringCellValue();
                String id = row.getCell(0).getStringCellValue();
                String description = row.getCell(2).getStringCellValue();
                String nameActivity = row.getCell(4).getStringCellValue();
                String descriptionActivity = row.getCell(5).getStringCellValue();
                TaskStatus statusTask = TaskStatus.valueOf(row.getCell(7).getStringCellValue());
                String descriptionTask = row.getCell(8).getStringCellValue();
                int durationTask = Integer.parseInt(row.getCell(9).getStringCellValue());

                CustomProcess processImport = new CustomProcess(name, description);
                Activity activityImport = new Activity(nameActivity, descriptionActivity);
                Task taskImport = new Task(descriptionTask, statusTask, durationTask);
                processManager.addProcess(processImport);
            }

            workbook.close();
            inputStream.close();
            System.out.println("¡Datos importados exitosamente desde el archivo!" + selectedFile);
            CAlert.Alert(Alert.AlertType.CONFIRMATION,"Successfully Imported","Data has been imported successfully","Closed");
        } catch (IOException e) {
            e.printStackTrace();
            CAlert.Alert(Alert.AlertType.ERROR,"Wrong Import","Data could not be imported","Closed");

        }
    }
}
