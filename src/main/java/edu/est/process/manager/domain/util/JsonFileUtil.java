package edu.est.process.manager.domain.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Clase utilitaria para la manipulación de archivos JSON.
 */
public class JsonFileUtil {

    /**
     * Guarda un objeto JSON en un archivo en la ruta especificada.
     *
     * @param jsonObject El objeto JSON a guardar en el archivo.
     * @param filePath   Ruta donde se guardará el archivo JSON.
     */
    public static void saveJsonObjectToFile(JsonObject jsonObject, String filePath) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonObject.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Lee un archivo JSON desde la ruta especificada y devuelve su representación como un objeto JSON.
     *
     * @param filePath Ruta del archivo JSON a leer.
     * @return Un objeto JSON leído desde el archivo, o null si ocurre un error durante la lectura.
     */
    public static JsonObject readJsonFromFile(String filePath) {
        try (FileReader fileReader = new FileReader(filePath)) {
            return JsonParser.parseReader(fileReader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
