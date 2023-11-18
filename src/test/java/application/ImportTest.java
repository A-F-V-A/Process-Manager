package application;

import edu.est.process.manager.domain.util.Import;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ImportTest {

    private final String testCSVFilePath = "C:\\Users\\MI PC\\Documents\\IngenieriaDeSistemas\\Semestre5\\EstructuraDatos\\Process-Manager.CSV"; // Ruta del archivo CSV de prueba
    private final String testExcelFilePath = ":\\Users\\MI PC\\Documents\\IngenieriaDeSistemas\\Semestre5\\EstructuraDatos\\Process-Manager.xlsx"; // Ruta del archivo Excel de prueba

    @Test
    public void testImportFromCSV() {
        Import importer = new Import();
        List<String[]> csvData = importer.importFromCSV(testCSVFilePath);

        assertFalse(csvData.isEmpty(), "No se obtuvieron datos del archivo CSV");

        // Validación de columnas consistentes
        if (!csvData.isEmpty()) {
            int expectedColumnCount = csvData.get(0).length;
            csvData.forEach(row -> assertEquals(expectedColumnCount, row.length, "Todas las filas deberían tener la misma cantidad de columnas"));
        }
    }

    @Test
    public void testImportFromExcel() {
        Import importer = new Import();
        List<String[]> excelData = importer.importFromExcel(testExcelFilePath);

        assertFalse(excelData.isEmpty(), "No se obtuvieron datos del archivo Excel");

        // Validación de columnas consistentes
        if (!excelData.isEmpty()) {
            int expectedColumnCount = excelData.get(0).length;
            excelData.forEach(row -> assertEquals(expectedColumnCount, row.length, "Todas las filas deberían tener la misma cantidad de columnas"));
        }
    }


    @Test
    public void testImportFromNonExistingFile() {
        Import importer = new Import();
        List<String[]> data = importer.importFromExcel("ruta/invalida");

        assertTrue(data.isEmpty(), "No debería obtener datos de un archivo que no existe");
    }
}
