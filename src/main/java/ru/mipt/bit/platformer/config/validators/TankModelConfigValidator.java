package ru.mipt.bit.platformer.config.validators;

import java.io.InputStream;

import ru.mipt.bit.platformer.config.GameConfig;

public class TankModelConfigValidator {
    public static void validate(GameConfig.TankModelConfig config, String configName) {
        if (config.tankMovementSpeed <= 0) {
            throw new IllegalArgumentException(configName + ".json: movementSpeed must be positive, got " + config.tankMovementSpeed);
        }

        if (config.bulletMovementSpeed <= 0) {
            throw new IllegalArgumentException(configName + ".json: bulletMovementSpeed must be positive, got " + config.bulletMovementSpeed);
        }

        if (config.bulletDefaultDamage <= 0) {
            throw new IllegalArgumentException(configName + ".json: bulletDefaultDamage must be positive, got " + config.bulletDefaultDamage);
        }

        checkFilePathForExistence(config.tankTexturePath, configName);
        checkFilePathForExistence(config.bulletTexturePath, configName);
    }

    private static void checkFilePathForExistence(String filePath, String configName) {
        InputStream inputStream = TankModelConfigValidator.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException(configName + ".json: File not found: " + filePath);
        }
    }
}
