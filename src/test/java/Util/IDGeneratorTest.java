package Util;


import edu.est.process.manager.domain.util.IDGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

public class IDGeneratorTest {
    @Test
    public void generateID_ShouldReturnUniqueIDs() {
        Set<String> ids = new HashSet<>();
        final int numberOfIDs = 1000; // Número de IDs a generar para la prueba

        for (int i = 0; i < numberOfIDs; i++) {
            String id = IDGenerator.generateID();
            // Verificar que el ID no está ya en el conjunto de IDs
            Assertions.assertFalse(ids.contains(id));
            ids.add(id);
        }

        // Verificar que hemos generado la cantidad correcta de IDs únicos
        Assertions.assertEquals(numberOfIDs, ids.size());
    }
}
