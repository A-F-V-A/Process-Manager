package edu.est.process.manager.domain.util;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.poi.ss.usermodel.*;

/**
 * Clase que gestiona la exportación de datos a archivos CSV y Excel.
 */
public class Export {

    /**
     * Exporta los datos a un archivo CSV en la ruta especificada.
     *
     * @param data     Datos a exportar en forma de lista de matrices de cadenas.
     * @param filePath Ruta del archivo CSV de destino.
     * @throws IOException Si ocurre un error al escribir en el archivo CSV.
     */
    public static void exportToCSV(List<String[]> data, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (String[] row : data) {
                for (int i = 0; i < row.length; i++) {
                    writer.append(row[i]);
                    if (i < row.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
        }
    }

    /**
     * Exporta los datos a un archivo Excel en la ruta especificada.
     *
     * @param data     Datos a exportar en forma de lista de matrices de cadenas.
     * @param filePath Ruta del archivo Excel de destino.
     * @throws IOException Si ocurre un error al escribir en el archivo Excel.
     */
    public static void exportToExcel(List<String[]> data, String filePath) throws IOException {
        Workbook workbook = null;
        FileOutputStream fileOut = null;
        try {
            workbook = WorkbookFactory.create(true);
            Sheet sheet = workbook.createSheet("Datos");

            // Escribir los datos en la hoja de cálculo
            int rowNum = 0;
            for (String[] rowData : data) {
                Row row = sheet.createRow(rowNum++);
                int colNum = 0;
                for (String cellData : rowData) {
                    Cell cell = row.createCell(colNum++);
                    cell.setCellValue(cellData);
                }
            }

            // Guardar el archivo Excel si no existe previamente
            fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);

        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    // Manejar el cierre del workbook
                    e.printStackTrace();
                }
            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    // Manejar el cierre del FileOutputStream
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Exporta los datos a un archivo especificado según el tipo de archivo deseado (CSV o Excel).
     *
     * @param data     Datos a exportar en forma de lista de matrices de cadenas.
     * @param filePath Ruta del archivo de destino.
     * @param fileType Tipo de archivo deseado (CSV o Excel).
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public void exportData(List<String[]> data, String filePath, FileType fileType) throws IOException {
        switch (fileType) {
            case CSV:
                exportToCSV(data, filePath);
                break;
            case EXCEL:
                exportToExcel(data, filePath);
                break;
            default:
                throw new IllegalArgumentException("Tipo de archivo no compatible");
        }
    }

    /**
     * Enumeración que define los tipos de archivo soportados para la exportación.
     */
    public enum FileType {
        CSV,
        EXCEL
    }
}
