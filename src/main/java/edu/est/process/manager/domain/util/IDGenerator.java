package edu.est.process.manager.domain.util;

import java.util.UUID;
/**
 * Clase que genera id.
 */
public class IDGenerator {
    /**
     * Genera un identificador único como una cadena de texto.
     *
     * @return Una cadena de texto que representa un identificador único.
     */
    public static String generateID() {
        return UUID.randomUUID().toString();
    }
}
