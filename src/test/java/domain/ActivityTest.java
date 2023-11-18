package domain;


import edu.est.process.manager.domain.models.Activity;
import edu.est.process.manager.domain.models.Task;
import edu.est.process.manager.domain.models.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class ActivityTest {

    private Activity activity;
    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        activity = new Activity("Activity Name");
        task1 = new Task("Task 1", TaskStatus.MANDATORY, 30); // Asumiendo que tienes una clase Task y un enum TaskStatus
        task2 = new Task("Task 2", TaskStatus.OPTIONAL, 45);
    }

    @Test
    void addTask_ShouldAddToPendingTasks() {
        activity.addTask(task1);
        activity.addTask(task2);

        Assertions.assertEquals(2, activity.getPendingTasks().size());
        Assertions.assertTrue(activity.getPendingTasksAsList().contains(task1));
        Assertions.assertTrue(activity.getPendingTasksAsList().contains(task2));
    }

    @Test
    void completeTask_ShouldMoveTaskToCompletedTasks() {
        activity.addTask(task1);
        activity.addTask(task2);

        Task completedTask = activity.completeTask();

        assertEquals(task1, completedTask);
        assertEquals(1, activity.getPendingTasks().size());
        assertEquals(1, activity.getCompletedTasks().size());
        assertTrue(activity.getCompletedTasksAsList().contains(task1));
    }

    @Test
    void getTotalDurationMinutes_ShouldReturnCorrectTotalTime() {
        activity.addTask(task1);
        activity.addTask(task2);
        activity.completeTask(); // Complete una tarea

        int expectedTotalDuration = task1.getDurationMinutes() + task2.getDurationMinutes();
        Assertions.assertEquals(expectedTotalDuration, activity.getTotalDurationMinutes());
    }
}

