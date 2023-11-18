package domain;

import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.models.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomProcessTest {

    private CustomProcess process;
    private Activity activity1;
    private Activity activity2;

    @BeforeEach
    void setUp() {
        process = new CustomProcess("Process Name");
        activity1 = new Activity("Activity 1");
        activity2 = new Activity("Activity 2");

        // Asumiendo que cada tarea añadida tiene una duración definida
        activity1.addTask(new Task("Task 1", TaskStatus.MANDATORY, 30));
        activity2.addTask(new Task("Task 2", TaskStatus.MANDATORY, 45));
    }

    @Test
    void addActivity_ShouldIncreaseTotalDuration() {
        process.addActivity(activity1);
        Assertions.assertEquals(30, process.getTotalDurationMinutes());

        process.addActivity(activity2);
        Assertions.assertEquals(75, process.getTotalDurationMinutes());
    }

    @Test
    void getActivity_ShouldReturnCorrectActivity() {
        process.addActivity(activity1);
        process.addActivity(activity2);

        Activity foundActivity = process.getActivity(activity1.getId());
        Assertions.assertNotNull(foundActivity);
        Assertions.assertEquals(activity1.getId(), foundActivity.getId());
    }

    @Test
    void removeActivity_ShouldDecreaseTotalDuration() {
        process.addActivity(activity1);
        process.addActivity(activity2);

        Assertions.assertTrue(process.removeActivity(activity1.getId()));
        Assertions.assertEquals(45, process.getTotalDurationMinutes());
    }

    @Test
    void removeActivity_ShouldReturnFalseIfActivityNotFound() {
        process.addActivity(activity1);
        Assertions.assertFalse(process.removeActivity("nonExistingId"));
    }
}
