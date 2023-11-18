package edu.est.process.manager.domain.models;


import java.io.*;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import edu.est.process.manager.domain.util.JsonFileUtil;

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
}