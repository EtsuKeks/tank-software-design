package ru.mipt.bit.platformer.config;

import com.google.gson.Gson;
import ru.mipt.bit.platformer.config.validators.LevelConfigValidator;
import ru.mipt.bit.platformer.config.validators.TankModelConfigValidator;
import ru.mipt.bit.platformer.config.validators.TreeModelConfigValidator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigLoader {
    public static GameConfig loadConfig(String folderPath) throws IOException {
        Gson gson = new Gson();

        GameConfig gameConfig = new GameConfig();

        gameConfig.playerTankModelConfig = loadSubConfig(folderPath + "/playerTankModelConfig.json", GameConfig.TankModelConfig.class, gson);
        TankModelConfigValidator.validate(gameConfig.playerTankModelConfig, "playerTankModelConfig");

        gameConfig.botTankModelConfig = loadSubConfig(folderPath + "/botTankModelConfig.json", GameConfig.TankModelConfig.class, gson);
        TankModelConfigValidator.validate(gameConfig.botTankModelConfig, "botTankModelConfig");

        gameConfig.treeModelConfig = loadSubConfig(folderPath + "/treeModelConfig.json", GameConfig.TreeModelConfig.class, gson);
        TreeModelConfigValidator.validate(gameConfig.treeModelConfig);

        gameConfig.levelConfig = loadSubConfig(folderPath + "/levelConfig.json", GameConfig.LevelConfig.class, gson);
        LevelConfigValidator.validate(gameConfig.levelConfig);

        return gameConfig;
    }

    private static <T> T loadSubConfig(String filePath, Class<T> clazz, Gson gson) throws IOException {
        InputStream inputStream = checkFilePathForExistence(filePath);

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return gson.fromJson(reader, clazz);
        }
    }

    private static InputStream checkFilePathForExistence(String filePath) {
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + filePath);
        }

        return inputStream;
    }
}
