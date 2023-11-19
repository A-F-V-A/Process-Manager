package edu.est.process.manager.domain.util;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.est.process.manager.domain.models.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase que gestiona la exportación de datos a archivos CSV y Excel.
 */
public class Export {

    public static void exportToExcel(String filePath) throws IOException {
        Export export = new Export();
        List<String[]> data = export.loadDataForExcel();

        try (Workbook workbook = WorkbookFactory.create(true);
             FileOutputStream fileOut = new FileOutputStream(filePath)) {

            Sheet sheet = workbook.createSheet("Datos");

            // Encabezados de las columnas
            String[] headers = {
                    "nombre proceso", "idProceso", "DescripcionProceso", "duracionProceso",
                    "idActividades", "nombre actividad", "actividad", "nombreDescripcion",
                    "idTarea", "descripcion", "estado", "duracion"
            };
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            int rowNum = 1;
            for (String[] rowData : data) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (String cellData : rowData) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(cellData);
                }
            }

            workbook.write(fileOut);
        }
    }    public List<String[]> loadDataForExcel() {
        List<String[]> excelData = new ArrayList<>();

        ProcessManager processManager = ProcessManager.getInstance();
        Map<String, CustomProcess> processes = processManager.getProcesses();

        for (CustomProcess process : processes.values()) {
            excelData.add(getProcessInfo(process)); // Datos del proceso

            // Datos de las actividades
            for (Activity activity : process.getActivities().toList()) {
                excelData.add(getActivityInfo(process, activity)); // Datos de la actividad
                excelData.addAll(getPendingTaskInfo(process, activity)); // Datos de tareas pendientes
                excelData.addAll(getCompletedTaskInfo(process, activity)); // Datos de tareas completadas
            }
        }

        return excelData;
    }

    private String[] getProcessInfo(CustomProcess process) {
        return new String[]{
                process.getName(),
                process.getId(),
                process.getDescription(),
                String.valueOf(process.getTotalDurationMinutes())
        };
    }

    private String[] getActivityInfo(CustomProcess process, Activity activity) {
        return new String[]{
                process.getName(), // Nombre del proceso al que pertenece la actividad
                activity.getId(),
                activity.getDescription(),
        };
    }

    private List<String[]> getPendingTaskInfo(CustomProcess process, Activity activity) {
        List<String[]> pendingTasksInfo = new ArrayList<>();
        for (Task task : activity.getPendingTasksAsList()) {
            String[] pendingTaskInfo = {
                    process.getName(), // Nombre del proceso al que pertenece la tarea
                    activity.getId(), // ID de la actividad a la que pertenece la tarea
                    activity.getDescription(),
                    task.getDescription(),
                    String.valueOf(task.getStatus()),
                    String.valueOf(task.getDurationMinutes())
            };
            pendingTasksInfo.add(pendingTaskInfo);
        }
        return pendingTasksInfo;
    }

    private List<String[]> getCompletedTaskInfo(CustomProcess process, Activity activity) {
        List<String[]> completedTasksInfo = new ArrayList<>();
        for (Task task : activity.getCompletedTasksAsList()) {
            String[] completedTaskInfo = {
                    process.getName(), // Nombre del proceso al que pertenece la tarea
                    activity.getId(), // ID de la actividad a la que pertenece la tarea
                    task.getDescription(),
                    String.valueOf(task.getStatus()),
                    String.valueOf(task.getDurationMinutes())
            };
            completedTasksInfo.add(completedTaskInfo);
        }
        return completedTasksInfo;
    }
}
