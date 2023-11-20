package application;

import com.github.javafaker.Faker;
import edu.est.process.manager.domain.util.Export;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

            List<String[]> importedData = export.importFromExcel(filePath);

            // Verificar que se haya cargado algún dato como al momento no tenemos datos fallara.
            assertNotNull(importedData);
            assertFalse(importedData.isEmpty());

            int expectedColumns = 12; // Ajustar según las columnas esperadas
            for (String[] rowData : importedData) {
                assertEquals(expectedColumns, rowData.length);
            }


        } catch (IOException e) {
            fail("Se produjo una excepción al exportar a Excel: " + e.getMessage());
        }
    }
    @Test
    public void testLoadDataForExcel() {
        Export export = new Export();

        // Simular datos de prueba
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"Proceso A", "001", "Descripción A", "30", "Act 1", "Actividad 1", "1", "Desc 1", "Tarea 1", "Desc Tarea 1", "Pendiente", "20"});
        testData.add(new String[]{"Proceso B", "002", "Descripción B", "25", "Act 2", "Actividad 2", "2", "Desc 2", "Tarea 2", "Desc Tarea 2", "Completada", "15"});

        // Sobrescribir el método loadDataForExcel para devolver datos de prueba
        export = new Export() {
            @Override
            public List<String[]> loadDataForExcel() {
                return testData;
            }
        };

        // Obtener los datos cargados
        List<String[]> loadedData = export.loadDataForExcel();

        // Verificar que se haya cargado algún dato
        assertNotNull(loadedData);
        assertFalse(loadedData.isEmpty());

        // Verificar el número de columnas esperadas
        int expectedColumns = 12;
        for (String[] rowData : loadedData) {
            assertEquals(expectedColumns, rowData.length);
        }
    }

    @Test
    public void testLoadDataForExcel2() {
        Export exportImport = new Export();

        List<String[]> excelData = new ArrayList<>();

        // Generamos datos aleatorios con Faker
        Faker faker = new Faker();
        int numOfProcesses = 3; // Número de procesos aleatorios

        for (int i = 0; i < numOfProcesses; i++) {
            String processName = faker.company().name();
            String processDescription = faker.lorem().sentence();

            // Agregamos datos del proceso al excelData
            excelData.add(new String[]{processName, "ProcessID_" + i, processDescription, String.valueOf(faker.number().numberBetween(100, 1000))});

            // Generamos actividades aleatorias para cada proceso
            int numOfActivities = faker.number().numberBetween(2, 5); // Número de actividades aleatorias por proceso

            for (int j = 0; j < numOfActivities; j++) {
                String activityName = faker.book().title();
                String activityDescription = faker.lorem().sentence();

                // Agregamos datos de la actividad al excelData
                excelData.add(new String[]{processName, "ActivityID_" + i + "_" + j, activityDescription});

                // Generamos tareas aleatorias para cada actividad
                int numOfTasks = faker.number().numberBetween(1, 4); // Número de tareas aleatorias por actividad

                for (int k = 0; k < numOfTasks; k++) {
                    String taskDescription = String.valueOf(faker.lorem().words());
                    String taskStatus = "COMPLETED"; // Podrías generar aleatoriamente estados
                    String taskDuration = String.valueOf(faker.number().numberBetween(5, 120)); // Duración aleatoria en minutos

                    // Agregamos datos de la tarea al excelData
                    excelData.add(new String[]{processName, "ActivityID_" + i + "_" + j, activityDescription, taskDescription, taskStatus, taskDuration});
                }
            }
        }

        // Realizamos la prueba
        exportImport.loadDataForExcel();
        List<String[]> testData = exportImport.loadDataForExcel();

        assertNotNull(testData); // Verificamos que los datos no sean nulos
        assertEquals(excelData.size(), testData.size()); // Comparamos la cantidad de datos
        // Aquí podrías realizar más aserciones para validar la estructura y contenido de los datos
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
