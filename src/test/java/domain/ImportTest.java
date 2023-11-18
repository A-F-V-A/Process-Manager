package domain;

import edu.est.process.manager.domain.models.Import;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ImportTest {

    private final String testCSVFilePath = "C:\\Users\\MI PC\\Desktop.csv"; // Ruta del archivo CSV de prueba
    private final String testExcelFilePath = "C:\\Users\\MI PC\\Downloads\\BORRADOR NOTAS SI 01N 2023-02.xlsx"; // Ruta del archivo Excel de prueba

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
