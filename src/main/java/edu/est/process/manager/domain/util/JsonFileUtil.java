package edu.est.process.manager.domain.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFileUtil {

    public static void saveJsonObjectToFile(JsonObject jsonObject, String filePath) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonObject.toString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JsonObject readJsonFromFile(String filePath) {
        try (FileReader fileReader = new FileReader(filePath)) {
            return JsonParser.parseReader(fileReader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
