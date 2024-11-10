package ru.mipt.bit.platformer.config;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigLoader {
    public static GameConfig loadConfig(String filePath) throws IOException {
        Gson gson = new Gson();
        
        InputStream inputStream = checkFilePathForExistence(filePath);

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            GameConfig config = gson.fromJson(reader, GameConfig.class);
            return checkConfig(config);
        }
    }

    private static GameConfig checkConfig(GameConfig config) throws IllegalArgumentException, IOException {
        if (config.tankMovementSpeed <= 0) {
            throw new IllegalArgumentException("In config.json tankMovementSpeed must be nonnegative, got " + config.tankMovementSpeed);
        }

        if (config.bulletMovementSpeed <= 0) {
            throw new IllegalArgumentException("In config.json bulletMovementSpeed must be nonnegative, got " + config.bulletMovementSpeed);
        }

        if (config.bulletDefaultDamage <= 0) {
            throw new IllegalArgumentException("In config.json bulletDefaultDamage must be nonnegative, got " + config.bulletDefaultDamage);
        }

        checkFilePathForExistence(config.tankTexturePath);
        checkFilePathForExistence(config.treeTexturePath);
        checkFilePathForExistence(config.bulletTexturePath);

        if (!(config.levelType.equals("random") || config.levelType.equals("file"))) {
            throw new IllegalArgumentException("In config.json levelType must be either \"random\" or \"file\", got " + config.levelType);
        }

        checkFilePathForExistence(config.levelDescPath);
        
        if (config.mapSize.height <= 0 || config.mapSize.width <= 0) {
            throw new IllegalArgumentException("In config.json mapsize must be nonnegative, got " + 
                config.mapSize.width + "x" + config.mapSize.height);
        }

        if (config.treesMinCount <= 0 || config.treesMinCount > config.treesMaxCount) {
            throw new IllegalArgumentException("In config.json it is needed to preserve 0 < treesMinCount <= treesMaxCount, got " +
                "treesMinCount = " + config.treesMinCount +
                "; treesMaxCount = " + config.treesMaxCount);
        }

        if (config.botTanksMinCount <= 0 || config.botTanksMinCount > config.botTanksMaxCount) {
            throw new IllegalArgumentException("In config.json it is needed to preserve 0 < botTanksMinCount <= botTanksMaxCount, got " +
                "botTanksMinCount = " + config.botTanksMinCount +
                "; botTanksMaxCount = " + config.botTanksMaxCount);
        }

        return config;
    }

    private static InputStream checkFilePathForExistence(String filePath) {
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + filePath);
        }

        return inputStream;
    }
}