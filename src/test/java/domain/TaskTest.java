package domain;

import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.models.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {

    private Task task;

    @BeforeEach
    void setUp() {
        // Inicializa una tarea antes de cada prueba
        task = new Task("Test task", TaskStatus.MANDATORY, 60);
    }

    @Test
    void taskInitializationTest() {
        Assertions.assertEquals("Test task", task.getDescription());
        Assertions.assertEquals(TaskStatus.MANDATORY, task.getStatus());
        Assertions.assertEquals(60, task.getDurationMinutes());
    }

    @Test
    void completeTaskTest() {
        task.completeTask();
        Assertions.assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    void setTaskStatusTest() {
        task.setStatus(TaskStatus.OPTIONAL);
        Assertions.assertEquals(TaskStatus.OPTIONAL, task.getStatus());
    }

    @Test
    void setDurationMinutesTest() {
        task.setDurationMinutes(30);
        Assertions.assertEquals(30, task.getDurationMinutes());
    }

}


