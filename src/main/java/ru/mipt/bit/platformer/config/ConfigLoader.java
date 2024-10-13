package ru.mipt.bit.platformer.config;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigLoader {
    public static GameConfig loadConfig(String filePath) throws IOException {
        Gson gson = new Gson();
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + filePath);
        }

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return gson.fromJson(reader, GameConfig.class);
        }
    }
}