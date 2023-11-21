package edu.est.process.manager.domain.models;


import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
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

    /**
     * Obtiene todos los procesos almacenados.
     *
     * @return un mapa con todos los procesos almacenados.
     */
    public Map<String, CustomProcess> getProcesses() {
        return processes;
    }

    /**
     * Guarda los datos de los procesos en un archivo JSON.
     */
    public void saveData(){
        JsonObject fileSave = new JsonObject();
        JsonArray processArray = new JsonArray();
        for (CustomProcess process : processes.values()) processArray.add(process.toJson());
        fileSave.add("process",processArray);
        saveJsonObjectToFile(fileSave,"processManager.json");
    }

    /**
     * Carga los datos de un archivo JSON a los procesos.
     */
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

    /**
     * Crea un objeto CustomProcess a partir de un JsonObject.
     *
     * @param process el JsonObject que contiene la información del proceso.
     * @return un objeto CustomProcess creado a partir del JsonObject.
     */
    private CustomProcess createProcess(JsonObject process) {
        CustomProcess loadProcess = new CustomProcess();

        loadProcess.setId(process.get("id").getAsString());
        loadProcess.setName(process.get("name").getAsString());
        loadProcess.setDescription(process.get("description").getAsString());

        return loadProcess;
    }

    /**
     * Rellena las actividades de un proceso a partir de un JsonObject.
     *
     * @param Objectivity el JsonObject que contiene la información de las actividades.
     * @param process     el CustomProcess al que se agregarán las actividades.
     */
    private void fillActivity(JsonObject Objectivity,CustomProcess process) {
        JsonArray activityArray = Objectivity.getAsJsonArray("activities");
        for (int i = 0; i < activityArray.size();i++){
            Activity activity = createActivity(activityArray.get(i).getAsJsonObject());
            fillTask(activityArray.get(i).getAsJsonObject(),activity);
            process.addActivity(activity);
        }
    }

    /**
     * Crea un objeto Activity a partir de un JsonObject.
     *
     * @param activity el JsonObject que contiene la información de la actividad.
     * @return un objeto Activity creado a partir del JsonObject.
     */
    private Activity createActivity(JsonObject activity) {
        Activity loadActivity = new Activity();

        loadActivity.setId(activity.get("id").getAsString());
        loadActivity.setName(activity.get("name").getAsString());
        loadActivity.setDescription(activity.get("description").getAsString());

        return loadActivity;
    }

    /**
     * Rellena las tareas de una actividad a partir de un JsonObject.
     *
     * @param Objectivity el JsonObject que contiene la información de las tareas.
     * @param activity    la Activity a la que se agregarán las tareas.
     */
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

    /**
     * Crea un objeto Task a partir de un JsonObject.
     *
     * @param task el JsonObject que contiene la información de la tarea.
     * @return un objeto Task creado a partir del JsonObject.
     */
    private Task createTask(JsonObject task) {
        Task newTask = new Task();

        newTask.setId(task.get("id").getAsString());
        newTask.setDurationMinutes(Integer.valueOf(task.get("durationMinutes").getAsString()));
        newTask.setStatus(TaskStatus.valueOf(task.get("status").getAsString().toUpperCase()));
        newTask.setDescription(task.get("description").getAsString());

        return newTask;
    }

    /**
     * Guarda los datos de los procesos en un archivo Excel.
     *
     * @param fileExcel la ruta del archivo Excel donde se guardarán los datos.
     */
    public void saveDataExcel(String fileExcel) {

        JsonObject data = JsonFileUtil.readJsonFromFile("processManager.json");
        if (data == null) return;;

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Process Data");
        int rowNum = 0;

        JsonArray processArray = data.getAsJsonArray("process");

        for (int i = 0; i < processArray.size(); i++) {
            JsonObject processObject = processArray.get(i).getAsJsonObject();
            CustomProcess process = createProcess(processObject);

            Row row = sheet.createRow(rowNum++);
            writeProcessDataToRow(row, process);

            JsonArray activityArray = processObject.getAsJsonArray("activities");
            for (int j = 0; j < activityArray.size(); j++) {
                JsonObject activityObject = activityArray.get(j).getAsJsonObject();
                Activity activity = createActivity(activityObject);

                Row activityRow = sheet.createRow(rowNum++);
                writeActivitiesDataToRow(activityRow, activity);

                JsonArray pendingTasksArray = activityObject.getAsJsonArray("pendingTasks");
                for (int k = 0; k < pendingTasksArray.size(); k++) {
                    JsonObject pendingTaskObject = pendingTasksArray.get(k).getAsJsonObject();
                    Task pendingTask = createTask(pendingTaskObject);

                    Row pendingTaskRow = sheet.createRow(rowNum++);
                    writeTaskDataToRow(pendingTaskRow, pendingTask);
                }

                JsonArray completedTasksArray = activityObject.getAsJsonArray("completedTasks");
                for (int k = 0; k < completedTasksArray.size(); k++) {
                    JsonObject completedTaskObject = completedTasksArray.get(k).getAsJsonObject();
                    Task completedTask = createTask(completedTaskObject);

                    Row completedTaskRow = sheet.createRow(rowNum++);
                    writeTaskDataToRow(completedTaskRow, completedTask);
                }
            }

            fillActivityAndTasks(processObject, process, sheet, rowNum);
        }

        try(FileOutputStream fileOut = new FileOutputStream(fileExcel)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Rellena las actividades y las tareas de un proceso a partir de un JsonObject y las escribe en una hoja Excel.
     *
     * @param processObject el JsonObject que contiene la información de las actividades y tareas.
     * @param process       el CustomProcess al que se agregarán las actividades.
     * @param sheet         la hoja Excel donde se escribirán los datos.
     * @param rowNum        el número de fila donde se comenzará a escribir.
     */
    private void fillActivityAndTasks(JsonObject processObject, CustomProcess process, Sheet sheet, int rowNum) {

        JsonArray activityArray = processObject.getAsJsonArray("activities");

        for (int i = 0; i < activityArray.size(); i++) {
            Activity activity = createActivity(activityArray.get(i).getAsJsonObject());

            fillTask(activityArray.get(i).getAsJsonObject(), activity);
            process.addActivity(activity);

            Row row = sheet.createRow(rowNum++);
            // Escribir los datos de la actividad en la fila correspondiente
            writeActivitiesDataToRow(row, activity);

            // Aumentar rowNum para la siguiente fila si hay más actividades o tareas
            rowNum = fillTasksIntoSheet(activity, sheet, rowNum);
        }

    }

    /**
     * Rellena las tareas de una actividad en una hoja Excel y aumenta el número de fila.
     *
     * @param activity la Activity de la que se obtendrán las tareas.
     * @param sheet    la hoja Excel donde se escribirán los datos.
     * @param rowNum   el número de fila donde se comenzará a escribir.
     * @return el número de fila actualizado después de rellenar las tareas.
     */
    private int fillTasksIntoSheet(Activity activity, Sheet sheet, int rowNum) {

        List<Task> pendingTasks = (List<Task>) activity.getPendingTasks();
        List<Task> completedTasks = (List<Task>) activity.getCompletedTasks();

        for (Task task : pendingTasks) {
            Row row = sheet.createRow(rowNum++);
            // Escribir los datos de la tarea pendiente en la fila correspondiente
            writeTaskDataToRow(row, task);
        }

        for (Task task : completedTasks) {
            Row row = sheet.createRow(rowNum++);
            // Escribir los datos de la tarea completada en la fila correspondiente
            writeTaskDataToRow(row, task);
        }

        return rowNum;

    }

    /**
     * Escribe los datos de una tarea en una fila de una hoja Excel.
     *
     * @param row  la fila donde se escribirán los datos de la tarea.
     * @param task la Task de la que se obtendrán los datos.
     */
    private void writeTaskDataToRow(Row row, Task task) {
        row.createCell(7).setCellValue(task.getId());
        row.createCell(8).setCellValue(task.getStatus().ordinal());
        row.createCell(9).setCellValue(task.getDescription());
        row.createCell(10).setCellValue(task.getDurationMinutes());
    }

    /**
     * Escribe los datos de una actividad en una fila de una hoja Excel.
     *
     * @param row      la fila donde se escribirán los datos de la actividad.
     * @param activity la Activity de la que se obtendrán los datos.
     */
    private void writeActivitiesDataToRow(Row row, Activity activity) {
        row.createCell(4).setCellValue(activity.getId());
        row.createCell(5).setCellValue(activity.getName());
        row.createCell(6).setCellValue(activity.getDescription());
    }

    /**
     * Escribe los datos de un CustomProcess en una fila de una hoja Excel.
     *
     * @param row     la fila donde se escribirán los datos del CustomProcess.
     * @param process el CustomProcess del que se obtendrán los datos.
     */
    private void writeProcessDataToRow(Row row, CustomProcess process) {
        row.createCell(0).setCellValue(process.getId());
        row.createCell(1).setCellValue(process.getName());
        row.createCell(2).setCellValue(process.getDescription());
        row.createCell(3).setCellValue(process.getTotalDurationMinutes());
    }

    /**
     * Lee datos desde un archivo Excel, los formatea como JSON y los guarda en un archivo.
     *
     * @param excelFilePath la ruta del archivo Excel.
     * @param jsonFilePath  la ruta del archivo JSON donde se guardarán los datos.
     */
    public void  readDataFromExcelAndSaveAsJson(String excelFilePath, String jsonFilePath) {

        Map<String, List<?>> dataFromExcel = readDataFromExcel(excelFilePath);
        JsonObject jsonToSave = formatDataToJson(dataFromExcel);
        saveJsonObjectToFile(jsonToSave, jsonFilePath);
    }

    /**
     * Lee datos desde un archivo Excel y los devuelve como un mapa con listas.
     *
     * @param filePath la ruta del archivo Excel.
     * @return un mapa que contiene datos leídos del archivo Excel.
     */
    private Map<String, List<?>> readDataFromExcel(String filePath) {
        Map<String, List<?>> excelData = new HashMap<>();

        try(FileInputStream fileInputStream = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(0);

            List<String> colum1Data = new ArrayList<>();
            List<String> colum2Data = new ArrayList<>();
            List<String> colum3Data = new ArrayList<>();
            List<String> colum4Data = new ArrayList<>();
            List<String> colum5Data = new ArrayList<>();
            List<String> colum6Data = new ArrayList<>();
            List<String> colum7Data = new ArrayList<>();
            List<String> colum8Data = new ArrayList<>();
            List<String> colum9Data = new ArrayList<>();
            List<String> colum10Data = new ArrayList<>();
            List<String> colum11Data = new ArrayList<>();
            List<String> colum12Data = new ArrayList<>();



            for (Row row : sheet) {

                colum1Data.add(row.getCell(0).getStringCellValue());
                colum2Data.add(row.getCell(1).getStringCellValue());
                colum3Data.add(row.getCell(2).getStringCellValue());
                colum4Data.add(row.getCell(3).getStringCellValue());
                colum5Data.add(row.getCell(4).getStringCellValue());
                colum6Data.add(row.getCell(5).getStringCellValue());
                colum7Data.add(row.getCell(6).getStringCellValue());
                colum8Data.add(row.getCell(7).getStringCellValue());
                colum9Data.add(row.getCell(8).getStringCellValue());
                colum10Data.add(row.getCell(9).getStringCellValue());
                colum11Data.add(row.getCell(10).getStringCellValue());
                colum12Data.add(row.getCell(11).getStringCellValue());
            }

            excelData.put("column1", colum1Data);
            excelData.put("column2", colum2Data);
            excelData.put("column3", colum3Data);
            excelData.put("column4", colum4Data);
            excelData.put("column5", colum5Data);
            excelData.put("column6", colum6Data);
            excelData.put("column7", colum7Data);
            excelData.put("column8", colum8Data);
            excelData.put("column9", colum9Data);
            excelData.put("column10", colum10Data);
            excelData.put("column11", colum11Data);

            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return excelData;
    }

    /**
     * Formatea datos leídos desde un archivo Excel como JSON.
     *
     * @param dataFromExcel los datos leídos desde un archivo Excel.
     * @return un JsonObject que contiene los datos formateados como JSON.
     */
    private JsonObject formatDataToJson(Map<String, List<?>> dataFromExcel) {
        JsonObject json = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        List<String> column1Data = (List<String>) dataFromExcel.get("column1");
        List<String> column2Data = (List<String>) dataFromExcel.get("column2");

        int rows = column1Data.size();
        for (int i = 0; i < rows; i++) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("column1", column1Data.get(i));
            jsonObject.addProperty("column2", column2Data.get(i));

            jsonArray.add(jsonObject);
        }

        json.add("data", jsonArray);

        return json;
    }

    /**
     * Guarda un JsonObject como archivo en la ruta especificada.
     *
     * @param jsonObject el JsonObject que se va a guardar como archivo.
     * @param filePath   la ruta donde se guardará el archivo.
     */
    private void saveJsonObjectToFile(JsonObject jsonObject, String filePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            Gson gson = new Gson();
            // Convertir el JsonObject a una cadena JSON y escribirlo en el archivo
            String jsonString = gson.toJson(jsonObject);
            fileOutputStream.write(jsonString.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene una lista de CustomProcess desde un archivo Excel.
     *
     * @param filePath la ruta del archivo Excel.
     * @return una lista de CustomProcess obtenida desde el archivo Excel.
     */
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

    /**
     * Obtiene una lista de Activity desde un archivo Excel.
     *
     * @param filePath la ruta del archivo Excel.
     * @return una lista de Activity obtenida desde el archivo Excel.
     */
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

    /**
     * Obtiene una lista de Task desde un archivo Excel.
     *
     * @param filePath la ruta del archivo Excel.
     * @return una lista de Task obtenida desde el archivo Excel.
     */
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

    /**
     * Carga CustomProcess desde un archivo Excel.
     *
     * @param filePath la ruta del archivo Excel.
     */
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

    /**
     * Carga Activity desde un archivo Excel.
     *
     * @param filePath la ruta del archivo Excel.
     */
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

    /**
     * Carga Task desde un archivo Excel.
     *
     * @param filePath la ruta del archivo Excel.
     */
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