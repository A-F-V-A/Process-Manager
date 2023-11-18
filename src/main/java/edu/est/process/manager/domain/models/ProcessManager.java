package edu.est.process.manager.domain.models;


import java.util.HashMap;
import java.util.Map;

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
}