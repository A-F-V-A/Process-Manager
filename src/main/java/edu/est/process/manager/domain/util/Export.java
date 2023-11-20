package edu.est.process.manager.domain.util;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.est.process.manager.domain.models.*;
import edu.est.process.manager.domain.structures.CustomDoublyLinkedList;
import edu.est.process.manager.domain.structures.CustomQueue;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Clase que gestiona la exportación e importacion de datos a archivos Excel.
 */
public class Export {

    private Map<String, CustomProcess> processes;


    public void saveData(String fileExcel) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Process Data");

        Map<String, Integer> headersMap = columnHeaders(); // Obtenemos los encabezados
        crearBaseExcel(workbook, sheet, headersMap); // Creamos la estructura del Excel

        int rowNum = 1; // Comenzamos desde la segunda fila para agregar datos

        for (CustomProcess process : processes.values()) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;

            // Datos del proceso
            row.createCell(colNum++).setCellValue(process.getName());
            row.createCell(colNum++).setCellValue(process.getId());
            row.createCell(colNum++).setCellValue(process.getDescription());
            row.createCell(colNum++).setCellValue(process.getTotalDurationMinutes());

            // Datos de actividades
            CustomDoublyLinkedList<Activity> activitiesList = process.getActivities();
            int size = activitiesList.size();

            for (int i = 0; i < size; i++) {
                Activity activity = activitiesList.get(i);
                Row activityRow = sheet.createRow(rowNum++);
                int activityColNum = 1; // Para omitir la primera columna si es necesario

                activityRow.createCell(activityColNum++).setCellValue(activity.getId());
                activityRow.createCell(activityColNum++).setCellValue(activity.getName());
                activityRow.createCell(activityColNum++).setCellValue(activity.getDescription());

                // Datos de tareas pendientes
                CustomQueue<Task> pendingTasksList = activity.getPendingTasks();
                int size2 = pendingTasksList.size();

                for (int j = 0; j < size2; j++) {
                    Task task = pendingTasksList.get(j);
                    Row taskRow = sheet.createRow(rowNum++);
                    int taskColNum = 2; // Para omitir las primeras dos columnas si es necesario

                    taskRow.createCell(taskColNum++).setCellValue(task.getId());
                    taskRow.createCell(taskColNum++).setCellValue(task.getDescription());
                    taskRow.createCell(taskColNum++).setCellValue(task.getStatus().ordinal());
                    taskRow.createCell(taskColNum++).setCellValue(task.getDurationMinutes());
                }

                // Datos de tareas completadas
                CustomQueue<Task> completedTasksList = activity.getCompletedTasks();
                int size3 = completedTasksList.size();

                for (int j = 0; j < size3; j++) {
                    Task task = completedTasksList.get(j);
                    Row taskRow = sheet.createRow(rowNum++);
                    int taskColNum = 2; // Para omitir las primeras dos columnas si es necesario

                    taskRow.createCell(taskColNum++).setCellValue(task.getId());
                    taskRow.createCell(taskColNum++).setCellValue(task.getDescription());
                    taskRow.createCell(taskColNum++).setCellValue(task.getStatus().ordinal());
                    taskRow.createCell(taskColNum++).setCellValue(task.getDurationMinutes());
                }
            }
        }

        FileOutputStream fileOut = new FileOutputStream(fileExcel);
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }

    public Map<String, Integer> columnHeaders() {
        Map<String, Integer> columnHeaders = new HashMap<>();
        columnHeaders.put("nombre proceso", 0);
        columnHeaders.put("idProceso", 1);
        columnHeaders.put("DescripcionProceso", 2);
        columnHeaders.put("duracionProceso", 3);
        columnHeaders.put("idActividades", 4);
        columnHeaders.put("nombre actividad", 5);
        columnHeaders.put("nombreDescripcion", 6);
        columnHeaders.put("idTarea", 7);
        columnHeaders.put("descripcion", 8);
        columnHeaders.put("estado", 9);
        columnHeaders.put("duracion", 10);

        return columnHeaders;
    }

    public void crearBaseExcel(Workbook workbook, Sheet sheet, Map<String, Integer> columnHeaders) {
        Row row = sheet.createRow(0);
        for (String columnHeader : columnHeaders.keySet()) {
            Cell cell = row.createCell(columnHeaders.get(columnHeader));
            cell.setCellValue(columnHeader);
        }
    }


    public List<String[]> importFromExcel(String filePath) throws IOException {
        if (!validateExcelFile(filePath)) {
            System.err.println("El archivo no cumple con los criterios requeridos.");
            return new ArrayList<>(); // O puedes manejarlo según la lógica de tu aplicación
        }

        List<String[]> importedData = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja

            // Iterar sobre las filas y columnas del archivo Excel
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                rowData.add(cell.getDateCellValue().toString());
                            } else {
                                rowData.add(String.valueOf(cell.getNumericCellValue()));
                            }
                            break;
                        case BOOLEAN:
                            rowData.add(String.valueOf(cell.getBooleanCellValue()));
                            break;
                        default:
                            rowData.add(""); // Agregar un valor vacío para otro tipo de celda
                    }
                }
                importedData.add(rowData.toArray(new String[0]));
            }
        }

        return importedData;
    }

    public boolean validateExcelFile(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return false; // No hay fila de encabezado
            }

            String[] expectedHeaders = {
                    "nombre proceso", "idProceso", "DescripcionProceso", "duracionProceso",
                    "idActividades", "nombre actividad", "nombreDescripcion",
                    "idTarea", "descripcion", "estado", "duracion"
            };

            // Verificar que todas las columnas esperadas estén presentes en el archivo
            for (int i = 0; i < expectedHeaders.length; i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null || !cell.getStringCellValue().trim().equalsIgnoreCase(expectedHeaders[i])) {
                    return false; // Columna faltante o nombre incorrecto
                }
            }

            // Verificar si hay datos en el archivo (al menos una fila de datos)
            return sheet.getLastRowNum() >= 1;

        } catch (IOException e) {
            e.printStackTrace();
            return false; // Error al leer el archivo
        }
    }
}
