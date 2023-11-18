package application;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import edu.est.process.manager.domain.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApplicationTest {
    @Test
    void createPersistence(){
        ProcessManager manager = ProcessManager.getInstance();

        CustomProcess codeProject = new CustomProcess(
                "Code Project", """
                    Process Manager is a Java application for managing processes and workflows.
                    It allows you to create, manage and coordinate activities and tasks efficiently,
                    optimizing the execution of complex projects.
        """);

        System.out.println(codeProject.getId());

        Activity createTest = new Activity("Create Test","Newly developed unit tests");
        Task persistence = new Task(
                "create test for persistence",
                TaskStatus.OPTIONAL,
                60);

        createTest.addTask(persistence);
        codeProject.addActivity(createTest);
        manager.addProcess(codeProject);

        manager.saveData();
        // Verificar que el archivo existe
        File file = new File("processManager.json");
        Assertions.assertTrue(file.exists(), "El archivo debería haber sido creado");

        // Opcional: Verificar que el contenido del archivo es el esperado
        try {
            String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
            JsonObject savedData = JsonParser.parseString(content).getAsJsonObject();
            // Aquí podrías añadir aserciones para verificar el contenido del JSON
            assertNotNull(savedData.get("process"), "El archivo debería contener la clave 'process'");
        } catch (Exception e) {
            Assertions.fail("No debería haber una excepción al leer el archivo");
        }

    }
    @Test
    void loadData(){
        ProcessManager manager = ProcessManager.getInstance();
        assertDoesNotThrow(manager::loadData, "loadData no debería lanzar una excepción.");
        // Limpiar después de la prueba
        File file = new File("processManager.json");
        if (file.exists()) {
            file.delete();
        }
    }
}

