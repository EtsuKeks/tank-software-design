package ru.mipt.bit.platformer.config.validators;

import java.io.InputStream;

import ru.mipt.bit.platformer.config.GameConfig;

public class LevelConfigValidator {
    public static void validate(GameConfig.LevelConfig config) {
        if (!(config.levelType.equals("random") || config.levelType.equals("file"))) {
            throw new IllegalArgumentException("levelConfig.json: levelType must be \"random\" or \"file\", got " + config.levelType);
        }

        if (config.mapSize.width <= 0 || config.mapSize.height <= 0) {
            throw new IllegalArgumentException("levelConfig.json: mapSize must have positive dimensions, got " +
                    config.mapSize.width + "x" + config.mapSize.height);
        }

        if (config.treesMinCount <= 0 || config.treesMinCount > config.treesMaxCount) {
            throw new IllegalArgumentException("levelConfig.json: 0 < treesMinCount <= treesMaxCount, got treesMinCount=" +
                    config.treesMinCount + ", treesMaxCount=" + config.treesMaxCount);
        }

        if (config.botTanksMinCount <= 0 || config.botTanksMinCount > config.botTanksMaxCount) {
            throw new IllegalArgumentException("levelConfig.json: 0 < botTanksMinCount <= botTanksMaxCount, got botTanksMinCount=" +
                    config.botTanksMinCount + ", botTanksMaxCount=" + config.botTanksMaxCount);
        }

        checkFilePathForExistence(config.levelDescPath);
    }

    private static void checkFilePathForExistence(String filePath) {
        InputStream inputStream = LevelConfigValidator.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("levelConfig.json: File not found: " + filePath);
        }
    }
}
