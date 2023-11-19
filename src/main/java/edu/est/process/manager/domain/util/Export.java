package edu.est.process.manager.domain.util;

import java.io.FileInputStream;
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
 * Clase que gestiona la exportación e importacion de datos a archivos Excel.
 */
public class Export {

    /**
     * Exporta los datos a un archivo Excel en el path especificado.
     *
     * @param filePath Ruta donde se guardará el archivo Excel.
     * @throws IOException Si ocurre un error durante la escritura del archivo.
     */
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
    }

    /**
     * Carga los datos para su exportación a un archivo Excel.
     *
     * @return Lista de arrays de strings que representan los datos a exportar.
     */
    public List<String[]> loadDataForExcel() {
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

    /**
     * Importa datos desde un archivo Excel en el path especificado.
     *
     * @param filePath Ruta del archivo Excel desde donde se importarán los datos.
     * @return Lista de arrays de strings que representan los datos importados.
     * @throws IOException Si ocurre un error durante la lectura del archivo.
     */
    public List<String[]> importFromExcel(String filePath) throws IOException {

        if (!validateExcelFile(filePath)) {
            System.err.println("El archivo no cumple con los criterios, puedes revisar si cuenta con todas las columnas");
            return new ArrayList<>(); // O puedes lanzar una excepción, dependiendo de la lógica de tu aplicación
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

    /**
     * Valida si el archivo Excel cumple con los criterios requeridos.
     *
     * @param filePath Ruta del archivo Excel a validar.
     * @return true si el archivo cumple con los criterios, false de lo contrario.
     */
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
                    "idActividades", "nombre actividad", "actividad", "nombreDescripcion",
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
