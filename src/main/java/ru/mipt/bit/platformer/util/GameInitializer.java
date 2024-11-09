package ru.mipt.bit.platformer.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.config.ConfigLoader;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.keepers.ModelZooKeeper;

import java.io.*;
import java.util.Random;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.logicmodels.Obstacle;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.logicmodels.TreeLogicModel;

public class GameInitializer {
    private final GameConfig config;
    private final ModelZooKeeper modelZooKeeper;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    public GameInitializer(GameConfig config, ModelZooKeeper modelZooKeeper, TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        this.config = config;
        this.modelZooKeeper = modelZooKeeper;
        this.groundLayer = groundLayer;
        this.tileMovement = tileMovement;
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
        initTree(new GridPoint2(-1, -1));
        initTree(new GridPoint2(-1, config.mapSize.height));
        initTree(new GridPoint2(config.mapSize.width, -1));
        initTree(new GridPoint2(config.mapSize.width, config.mapSize.height));

        for (int i = 0; i < config.mapSize.height; i++) {
            initTree(new GridPoint2(-1, i));
            initTree(new GridPoint2(config.mapSize.width, i));
        }

        for (int i = 0; i < config.mapSize.width; i++) {
            initTree(new GridPoint2(i, -1));
            initTree(new GridPoint2(i, config.mapSize.height));
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
                        initTree(new GridPoint2(x, y));
                        break;
                    case 'X':
                        initTank(new GridPoint2(x, y), true);
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
            initTree(generateRandomCoords());
        }

        initTank(generateRandomCoords(), true);
    }

    private void generateTankBotsRandomly() {
        Random random = new Random();
        int numBotTanks = random.nextInt(config.botTanksMaxCount - config.botTanksMinCount) + config.botTanksMinCount;

        for (int i = 0; i < numBotTanks; i++) {
            initTank(generateRandomCoords(), false);
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

    private void initTree(GridPoint2 coords) {
        Rectangle treeRectangle = initRectangle(groundLayer, config.treeTexturePath, coords);
        new TreeLogicModel(modelZooKeeper, treeRectangle, coords);
    }

    private void initTank(GridPoint2 coords, boolean playable) {
        Rectangle playerTankRectangle = initRectangle(groundLayer, config.tankTexturePath, coords);
        new TankLogicModel(modelZooKeeper, playable, playerTankRectangle, tileMovement, config.tankMovementSpeed,
                coords, modelZooKeeper.getObstacles());
    }

    private Rectangle initRectangle(TiledMapTileLayer groundLayer, String texturePath, GridPoint2 coordinate) {
        Texture texture = new Texture(texturePath);
        TextureRegion graphics = new TextureRegion(texture);
        Rectangle rectangle = GdxGameUtils.createBoundingRectangle(graphics);
        GdxGameUtils.moveRectangleAtTileCenter(groundLayer, rectangle, coordinate);
        return rectangle;
    }
}