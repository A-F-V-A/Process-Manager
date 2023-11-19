package application;

import edu.est.process.manager.domain.util.Export;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ExportTest {

    @Test
    public void testExportToExcel() {
        Export export = new Export();
        String filePath = "test.xlsx";

        try {
            export.exportToExcel(filePath);
            File file = new File(filePath);
            assertTrue(file.exists());
        } catch (IOException e) {
            fail("Se produjo una excepción al exportar a Excel: " + e.getMessage());
        }
    }

    @Test
    public void testLoadDataForExcel() {
        Export export = new Export();
        List<String[]> loadedData = export.loadDataForExcel();

        // Verificar que se haya cargado algún dato
        assertNotNull(loadedData);
        assertFalse(loadedData.isEmpty());

        // Verificar el número de columnas esperadas
        int expectedColumns = 12; // Ajustar según las columnas esperadas
        for (String[] rowData : loadedData) {
            assertEquals(expectedColumns, rowData.length);
        }
    }

    @Test
    public void testImportFromExcelValidFile() {
        String validFilePath = "C:\\Users\\MI PC\\Documents\\IngenieriaDeSistemas\\Semestre5\\EstructuraDatos\\Process-Manager\\test.xlsx"; 

        Export exporter = new Export();
        List<String[]> importedData = null;
        try {
            importedData = exporter.importFromExcel(validFilePath);
        } catch (IOException e) {
            fail("Excepción al importar desde Excel: " + e.getMessage());
        }

        assertNotNull(importedData);
        assertFalse(importedData.isEmpty());

        // Puedes realizar más validaciones según la estructura de tus datos esperados
        // Por ejemplo, verificar si la cantidad de columnas y filas coinciden con lo esperado.
    }

    @Test
    public void testImportFromExcelInvalidFile() {
        String invalidFilePath = "C:\\Users\\MI PC\\Documents\\IngenieriaDeSistemas\\Semestre5\\EstructuraDatos\\Process-Manager\\test.xlsx";

        Export exporter = new Export();
        List<String[]> importedData = null;
        try {
            importedData = exporter.importFromExcel(invalidFilePath);
        } catch (IOException e) {
            fail("Excepción al importar desde Excel: " + e.getMessage());
        }

        assertNotNull(importedData);
        assertTrue(importedData.isEmpty()); // Se espera una lista vacía ya que el archivo es inválido
    }
}
