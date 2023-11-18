package domain;


import edu.est.process.manager.domain.models.CustomProcess;
import edu.est.process.manager.domain.models.ProcessManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProcessManagerTest {
    @Test
    void getInstance_ShouldAlwaysReturnSameInstance() {
        ProcessManager manager1 = ProcessManager.getInstance();
        ProcessManager manager2 = ProcessManager.getInstance();

        assertSame(manager1, manager2, "getInstance debería retornar la misma instancia.");
    }

    @Test
    void processManagement_ShouldAddGetAndRemoveProcessesCorrectly() {
        ProcessManager manager = ProcessManager.getInstance();
        CustomProcess process1 = new CustomProcess("Process 1");
        CustomProcess process2 = new CustomProcess("Process 2");

        manager.addProcess(process1);
        manager.addProcess(process2);

        assertEquals(process1, manager.getProcess(process1.getId()), "El proceso obtenido debería ser igual al añadido.");
        assertEquals(process2, manager.getProcess(process2.getId()), "El proceso obtenido debería ser igual al añadido.");

        CustomProcess removedProcess = manager.removeProcess(process1.getId());
        assertEquals(process1, removedProcess, "El proceso eliminado debería ser igual al añadido.");

        assertNull(manager.getProcess(process1.getId()), "El proceso eliminado no debería encontrarse.");
        assertNotNull(manager.getProcess(process2.getId()), "El proceso no eliminado debería estar presente.");
    }
}
