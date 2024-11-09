package ru.mipt.bit.platformer.util;

import ru.mipt.bit.platformer.config.ConfigLoader;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.keepers.ModelZooKeeper;

import java.io.*;
import java.util.Random;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logicmodels.Obstacle;

public class GameInitializer {
    private final GameConfig config;
    private final ModelZooKeeper modelZooKeeper;

    public GameInitializer(GameConfig config, ModelZooKeeper modelZooKeeper) {
        this.modelZooKeeper = modelZooKeeper;
        this.config = config;
    }

    public void initialize() throws IOException {
        generateTreesOnEdges();

        if (config.levelType.equals("file")) {
            generateTreesAndPlayerTankFromFile();
        } else if (config.levelType.equals("random")) {
            generateTreesAndPlayerTankRandomly();
        }

        generateTankBotsRandomly();
    }

    private void generateTreesOnEdges() {
        modelZooKeeper.initTree(new GridPoint2(-1, -1));
        modelZooKeeper.initTree(new GridPoint2(-1, config.mapSize.height));
        modelZooKeeper.initTree(new GridPoint2(config.mapSize.width, -1));
        modelZooKeeper.initTree(new GridPoint2(config.mapSize.width, config.mapSize.height));

        for (int i = 0; i < config.mapSize.height; i++) {
            modelZooKeeper.initTree(new GridPoint2(-1, i));
            modelZooKeeper.initTree(new GridPoint2(config.mapSize.width, i));
        }

        for (int i = 0; i < config.mapSize.width; i++) {
            modelZooKeeper.initTree(new GridPoint2(i, -1));
            modelZooKeeper.initTree(new GridPoint2(i, config.mapSize.height));
        }
    }

    private void generateTreesAndPlayerTankFromFile() throws IOException {
        InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(config.levelDescPath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + config.levelDescPath);
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        int y = config.mapSize.height - 1;
        while ((line = reader.readLine()) != null && y >= 0) {
            for (int x = 0; x < Math.min(line.length(), config.mapSize.width); x++) {
                char cell = line.charAt(x);

                switch (cell) {
                    case 'T':
                        modelZooKeeper.initTree(new GridPoint2(x, y));
                        break;
                    case 'X':
                        modelZooKeeper.initPlayerTank(new GridPoint2(x, y));
                        break;
                    case '_':
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid character in level file: " + cell);
                }
            }
            y--;
        }
        reader.close();
    }

    private void generateTreesAndPlayerTankRandomly() {
        Random random = new Random();
        int numTrees = random.nextInt(config.treesMaxCount - config.treesMinCount) + config.treesMinCount;
        for (int i = 0; i < numTrees; i++) {
            modelZooKeeper.initTree(generateRandomCoords());
        }

        modelZooKeeper.initPlayerTank(generateRandomCoords());
    }

    private void generateTankBotsRandomly() {
        Random random = new Random();
        int numBotTanks = random.nextInt(config.botTanksMaxCount - config.botTanksMinCount) + config.botTanksMinCount;

        for (int i = 0; i < numBotTanks; i++) {
            modelZooKeeper.initBotTank(generateRandomCoords());
        }
    }

    private GridPoint2 generateRandomCoords() {
        Random random = new Random();
        GridPoint2 coords;
        do {
            coords = new GridPoint2(random.nextInt(config.mapSize.width), random.nextInt(config.mapSize.height));
        } while (isOccupied(coords));

        return coords;
    }

    private boolean isOccupied(GridPoint2 position) {
        for (Obstacle obstacle: modelZooKeeper.getObstacles()) {
            if (obstacle.isOccupied(position)) {
                return true;
            }
        }
        return false;
    }
}