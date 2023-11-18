package application;

import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.domain.util.Export;
import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.models.TaskStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ExportTest {

    @Test
    public void testExportToCSV() {
        Export export = new Export();
        List<String[]> testData = generateTestData();
        String filePath = "test.csv";

        assertDoesNotThrow(() -> export.exportToCSV(testData, filePath));
    }

    @Test
    public void testExportToExcel() {
        Export export = new Export();
        List<String[]> testData = generateTestData();
        String filePath = "test.xlsx";

        assertDoesNotThrow(() -> export.exportToExcel(testData, filePath));
    }

    @Test
    public void testExportDataCSV() {
        Export export = new Export();
        List<String[]> testData = generateTestData();
        String filePath = "data.csv";

        assertDoesNotThrow(() -> export.exportData(testData, filePath, Export.FileType.CSV));
    }

    @Test
    public void testExportDataExcel() {
        Export export = new Export();
        List<String[]> testData = generateTestData();
        String filePath = "data.xlsx";

        assertDoesNotThrow(() -> export.exportData(testData, filePath, Export.FileType.EXCEL));
    }

    // Método para generar datos de prueba
    private List<String[]> generateTestData() {
        List<String[]> testData = new ArrayList<>();

        // Crear una actividad con tareas asociadas
        Activity activity1 = new Activity("Activity 1");
        activity1.addTask(new Task("Task 1 for Activity 1", TaskStatus.PENDING, 60));
        activity1.addTask(new Task("Task 2 for Activity 1", TaskStatus.PENDING, 45));

        // Crear otra actividad con tareas asociadas
        Activity activity2 = new Activity("Activity 2");
        activity2.addTask(new Task("Task 1 for Activity 2", TaskStatus.PENDING, 30));
        activity2.addTask(new Task("Task 2 for Activity 2", TaskStatus.PENDING, 40));
        activity2.addTask(new Task("Task 3 for Activity 2", TaskStatus.PENDING, 55));

        // Exportar los datos de las actividades y sus tareas
        testData.addAll(exportActivity(activity1));
        testData.addAll(exportActivity(activity2));

        return testData;
    }

    private List<String[]> exportActivity(Activity activity) {
        List<String[]> exportData = new ArrayList<>();

        // Agregar información de la actividad
        exportData.add(new String[]{"Activity ID", "Activity Name"});
        exportData.add(new String[]{activity.getId(), activity.getName()});
        exportData.add(new String[]{""}); // Separador

        // Agregar información de las tareas de la actividad
        exportData.add(new String[]{"Task Description", "Status", "Duration (minutes)"});
        for (Task task : activity.getPendingTasksAsList()) {
            exportData.add(new String[]{task.getDescription(), task.getStatus().toString(), String.valueOf(task.getDurationMinutes())});
        }
        exportData.add(new String[]{""}); // Separador

        return exportData;
    }

}
