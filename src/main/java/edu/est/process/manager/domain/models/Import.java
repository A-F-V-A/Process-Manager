package edu.est.process.manager.domain.models;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona la importación de datos desde archivos CSV y Excel (XLSX).
 */
public class Import {

    /**
     * Importa datos desde un archivo CSV.
     *
     * @param filePath Ruta del archivo CSV del que se importarán los datos.
     * @return Lista de matrices de cadenas que representan los datos importados.
     */
    public List<String[]> importFromCSV(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Separador CSV
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Importa datos desde un archivo Excel (XLSX).
     *
     * @param filePath Ruta del archivo Excel del que se importarán los datos.
     * @return Lista de matrices de cadenas que representan los datos importados.
     */
    public List<String[]> importFromExcel(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                List<String> rowData = new ArrayList<>();
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            rowData.add(cell.getStringCellValue());
                            break;
                        case NUMERIC:
                            rowData.add(String.valueOf(cell.getNumericCellValue()));
                            break;
                        default:
                            rowData.add("");
                            break;
                    }
                }
                data.add(rowData.toArray(new String[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
