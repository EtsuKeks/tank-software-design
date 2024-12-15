package ru.mipt.bit.platformer.config.validators;

import java.io.InputStream;

import ru.mipt.bit.platformer.config.GameConfig;

public class TreeModelConfigValidator {
    public static void validate(GameConfig.TreeModelConfig config) {
        checkFilePathForExistence(config.texturePath);
    }

    private static void checkFilePathForExistence(String filePath) {
        InputStream inputStream = TreeModelConfigValidator.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("treeModelConfig.json: File not found: " + filePath);
        }
    }
}
