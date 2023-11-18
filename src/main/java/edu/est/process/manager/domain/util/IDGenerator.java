package edu.est.process.manager.domain.util;

import java.util.UUID;

public class IDGenerator {
    /**
     * Generates a unique identifier as a String.
     *
     * @return A unique identifier string.
     */
    public static String generateID() {
        return UUID.randomUUID().toString();
    }
}
