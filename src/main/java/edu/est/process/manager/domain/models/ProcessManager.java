package edu.est.process.manager.domain.models;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import edu.est.process.manager.domain.util.Export;
import edu.est.process.manager.domain.util.JsonFileUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ProcessManager {
    // Única instancia de ProcessManager
    private static ProcessManager instance;

    // Mapa para almacenar los procesos
    private Map<String, CustomProcess> processes;

    /**
     * Constructor privado para prevenir la instanciación desde fuera.
     */
    private ProcessManager() {
        processes = new HashMap<>();
    }

    /**
     * Método para obtener la única instancia de la clase.
     *
     * @return la única instancia de ProcessManager.
     */
    public static ProcessManager getInstance() {
        if (instance == null) {
            instance = new ProcessManager();
        }
        return instance;
    }

    /**
     * Añade un proceso al gestor.
     *
     * @param process el proceso a añadir.
     */
    public void addProcess(CustomProcess process) {
        processes.put(process.getId(), process);
    }

    /**
     * Obtiene un proceso por su ID.
     *
     * @param id el ID del proceso.
     * @return el proceso correspondiente, o null si no se encuentra.
     */
    public CustomProcess getProcess(String id) {
        return processes.get(id);
    }

    /**
     * Elimina un proceso por su ID.
     *
     * @param id el ID del proceso a eliminar.
     * @return el proceso eliminado, o null si no se encuentra.
     */
    public CustomProcess removeProcess(String id) {
        return processes.remove(id);
    }

    public Map<String, CustomProcess> getProcesses() {
        return processes;
    }

    public void saveData(){
        JsonObject fileSave = new JsonObject();
        JsonArray processArray = new JsonArray();
        for (CustomProcess process : processes.values()) processArray.add(process.toJson());
        fileSave.add("process",processArray);
        JsonFileUtil.saveJsonObjectToFile(fileSave,"processManager.json");
    }
    public void loadData(){
        JsonObject data = JsonFileUtil.readJsonFromFile("processManager.json");
        if(data == null) return;
        JsonArray processArray = data.getAsJsonArray("process");
        for (int i = 0; i < processArray.size();i++){
            CustomProcess process = createProcess(processArray.get(i).getAsJsonObject());
            fillActivity(processArray.get(i).getAsJsonObject(),process);
            addProcess(process);
        }
    }

    private CustomProcess createProcess(JsonObject process) {
        CustomProcess loadProcess = new CustomProcess();

        loadProcess.setId(process.get("id").getAsString());
        loadProcess.setName(process.get("name").getAsString());
        loadProcess.setDescription(process.get("description").getAsString());

        return loadProcess;
    }
    private void fillActivity(JsonObject Objectivity,CustomProcess process) {
        JsonArray activityArray = Objectivity.getAsJsonArray("activities");
        for (int i = 0; i < activityArray.size();i++){
            Activity activity = createActivity(activityArray.get(i).getAsJsonObject());
            fillTask(activityArray.get(i).getAsJsonObject(),activity);
            process.addActivity(activity);
        }
    }
    private Activity createActivity(JsonObject activity) {
        Activity loadActivity = new Activity();

        loadActivity.setId(activity.get("id").getAsString());
        loadActivity.setName(activity.get("name").getAsString());
        loadActivity.setDescription(activity.get("description").getAsString());

        return loadActivity;
    }
    private void fillTask(JsonObject Objectivity,Activity activity) {
        JsonArray pendingTasksArray   = Objectivity.getAsJsonArray("pendingTasks");
        JsonArray completedTasksArray = Objectivity.getAsJsonArray("completedTasks");
        for (int i = 0; i < pendingTasksArray.size();i++){
            Task task = createTask(pendingTasksArray.get(i).getAsJsonObject());
            activity.addTask(task);
        }
        for (int i = 0; i < completedTasksArray.size();i++){
            Task task = createTask(completedTasksArray.get(i).getAsJsonObject());
            activity.addCompletedTasks(task);
        }
    }
    private Task createTask(JsonObject task) {
        Task newTask = new Task();

        newTask.setId(task.get("id").getAsString());
        newTask.setDurationMinutes(Integer.valueOf(task.get("durationMinutes").getAsString()));
        newTask.setStatus(TaskStatus.valueOf(task.get("status").getAsString().toUpperCase()));
        newTask.setDescription(task.get("description").getAsString());

        return newTask;
    }

    public void saveDataExcel(String fileExcel) {
        Export exporter = new Export();
        try {
            exporter.saveData(fileExcel); // Invocar el método de exportación desde la instancia de Export
            getProcesses(fileExcel);
            getActivities(fileExcel);
            getTask(fileExcel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadDataExcel(String filePath) {
        Export export = new Export();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<String[]> importedData = export.importFromExcel(filePath);

            for (String[] rowData : importedData){
                for (String cellData : rowData) {
                    System.out.println(cellData + "\t");
                }
                System.out.println();
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Saltar la primera fila con los encabezados
                }
            }

            loadCustomProcess(filePath);

            // Cargar actividades
            loadActivities(filePath);

            // Cargar tareas
            loadTasks(filePath);

            ProcessManager  processManager = ProcessManager.getInstance();
            for (CustomProcess process : processes.values()) {
                processManager.addProcess(process);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<CustomProcess> getProcesses(String filePath) {
        List<CustomProcess> processes = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                CustomProcess process = new CustomProcess();

                process.setId(row.getCell(0).getStringCellValue());
                process.setName(row.getCell(1).getStringCellValue());
                process.setDescription(row.getCell(2).getStringCellValue());
                process.setTotalDurationMinutes((int) row.getCell(3).getNumericCellValue());

                processes.add(process);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return processes;
    }

    private List<Activity> getActivities(String filePath) {
        List<Activity> activities = new ArrayList<>();

        try(FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                Activity activity = new Activity();

                activity.setId(row.getCell(4).getStringCellValue());
                activity.setName(row.getCell(5).getStringCellValue());
                activity.setDescription(row.getCell(6).getStringCellValue());

                activities.add(activity);
            }

            workbook.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return activities;
    }

    private List<Task> getTask(String filePath) {
        List<Task> tasks = new ArrayList<>();

        try(FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                Task task = new Task();

                task.setId(row.getCell(7).getStringCellValue());
                task.setDescription(row.getCell(8).getStringCellValue());
                task.setStatus(TaskStatus.valueOf(row.getCell(9).getStringCellValue().toUpperCase()));
                task.setDurationMinutes((int) row.getCell(10).getNumericCellValue());

                tasks.add(task);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    private void loadCustomProcess(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterar sobre las filas y columnas del archivo Excel
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Saltar la primera fila con los encabezados
                }

                // Leer los valores de las celdas y crear instancias de CustomProcess
                String processId = row.getCell(0).getStringCellValue();
                String processName = row.getCell(1).getStringCellValue();
                String processDescription = row.getCell(2).getStringCellValue();
                int processTotalDuration = (int) row.getCell(3).getNumericCellValue();

                // Crear un objeto CustomProcess con los valores obtenidos
                CustomProcess process = new CustomProcess();
                process.setId(processId);
                process.setName(processName);
                process.setDescription(processDescription);
                process.setTotalDurationMinutes(processTotalDuration);

                // Añadir el CustomProcess al ProcessManager
                this.addProcess(process);
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadActivities(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterar sobre las filas y columnas del archivo Excel
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Saltar la primera fila con los encabezados
                }

                // Leer los valores de las celdas y crear instancias de Activity
                String activityId = row.getCell(4).getStringCellValue();
                String activityName = row.getCell(5).getStringCellValue();
                String activityDescription = row.getCell(6).getStringCellValue();

                // Crear un objeto Activity con los valores obtenidos
                Activity activity = new Activity();
                activity.setId(activityId);
                activity.setName(activityName);
                activity.setDescription(activityDescription);

                // Obtener el proceso al que pertenece esta actividad
                String processId = row.getCell(1).getStringCellValue();
                CustomProcess process = this.getProcess(processId);

                // Si el proceso existe, añadir la actividad a ese proceso
                if (process != null) {
                    process.addActivity(activity);
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTasks(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);
            Sheet sheet = workbook.getSheetAt(0);

            // Iterar sobre las filas y columnas del archivo Excel
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Saltar la primera fila con los encabezados
                }

                // Leer los valores de las celdas y crear instancias de Task
                String taskId = row.getCell(7).getStringCellValue();
                String taskDescription = row.getCell(8).getStringCellValue();
                String taskStatusString = row.getCell(9).getStringCellValue();
                TaskStatus taskStatus = TaskStatus.valueOf(taskStatusString.toUpperCase());
                int taskDuration = (int) row.getCell(10).getNumericCellValue();

                // Crear un objeto Task con los valores obtenidos
                Task task = new Task();
                task.setId(taskId);
                task.setDescription(taskDescription);
                task.setStatus(taskStatus);
                task.setDurationMinutes(taskDuration);

                // Obtener la actividad a la que pertenece esta tarea
                String activityId = row.getCell(4).getStringCellValue();
                CustomProcess process = this.getProcess(row.getCell(1).getStringCellValue());
                Activity activity = (process != null) ? process.getActivity(activityId) : null;

                // Si la actividad existe, añadir la tarea a esa actividad
                if (activity != null) {
                    activity.addTask(task);
                }
            }

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}