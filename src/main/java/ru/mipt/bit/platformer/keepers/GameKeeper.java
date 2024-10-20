package ru.mipt.bit.platformer.keepers;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;

import ru.mipt.bit.platformer.config.ConfigLoader;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;
import ru.mipt.bit.platformer.graphicmodels.TankGraphicModel;
import ru.mipt.bit.platformer.graphicmodels.TreeGraphicModel;
import ru.mipt.bit.platformer.logicmodels.Obstacle;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.logicmodels.TreeLogicModel;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

public class GameKeeper {
    public TankLogicModel playerTankLogicModel;
    public IGraphicModel playerTankGraphicModel;

    public final ArrayList<TankLogicModel> botTankLogicModels = new ArrayList<>();
    public final ArrayList<IGraphicModel> botTankGraphicModels = new ArrayList<>();

    public final ArrayList<TreeLogicModel> treeLogicModels = new ArrayList<>();
    public final ArrayList<IGraphicModel> treeGraphicModels = new ArrayList<>();

    private final GameConfig config;

    private GridPoint2 playerTankStartCoordinate;
    private final HashSet<GridPoint2> botTankStartCoordinates = new HashSet<>();
    private final HashSet<GridPoint2> treesStartCoordinates = new HashSet<>();

    private final ArrayList<Obstacle> obstacles = new ArrayList<>();

    public GameKeeper(String configFilePath, TiledMapTileLayer groundLayer, TileMovement tileMovement) throws IOException {
        config = ConfigLoader.loadConfig(configFilePath);

        generateTreesOnEdges();

        if (config.levelType.equals("file")) {
            generateTreesAndPlayerTankFromFile();
        } else if (config.levelType.equals("random")) {
            generateTreesAndPlayerTankRandomly();
        }

        generateTankBotsRandomly();

        initModels(groundLayer, tileMovement);
    }

    private void generateTreesOnEdges() {
        treesStartCoordinates.add(new GridPoint2(-1, -1));
        treesStartCoordinates.add(new GridPoint2(-1, config.mapSize.height));
        treesStartCoordinates.add(new GridPoint2(config.mapSize.width, -1));
        treesStartCoordinates.add(new GridPoint2(config.mapSize.width, config.mapSize.height));

        for (int i = 0; i < config.mapSize.height; i++) {
            treesStartCoordinates.add(new GridPoint2(-1, i));
            treesStartCoordinates.add(new GridPoint2(config.mapSize.width, i));
        }

        for (int i = 0; i < config.mapSize.width; i++) {
            treesStartCoordinates.add(new GridPoint2(i, -1));
            treesStartCoordinates.add(new GridPoint2(i, config.mapSize.height));
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
                        treesStartCoordinates.add(new GridPoint2(x, y));
                        break;
                    case 'X':
                        playerTankStartCoordinate = new GridPoint2(x, y);
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
            int x = random.nextInt(config.mapSize.width);
            int y = random.nextInt(config.mapSize.height);
            treesStartCoordinates.add(new GridPoint2(x, y));
        }

        do {
            playerTankStartCoordinate = new GridPoint2(random.nextInt(config.mapSize.width),
                                          random.nextInt(config.mapSize.height));
        } while (isOccupied(playerTankStartCoordinate));
    }

    private void generateTankBotsRandomly() {
        Random random = new Random();
        int numBotTanks = random.nextInt(config.botTanksMaxCount - config.botTanksMinCount) + config.botTanksMinCount;

        for (int i = 0; i < numBotTanks; i++) {
            GridPoint2 botTankStartCoordinate;

            do {
                botTankStartCoordinate = new GridPoint2(random.nextInt(config.mapSize.width),
                        random.nextInt(config.mapSize.height));
            } while (isOccupied(botTankStartCoordinate) || playerTankStartCoordinate.equals(botTankStartCoordinate));

            botTankStartCoordinates.add(botTankStartCoordinate);
        }
    }

    private boolean isOccupied(GridPoint2 position) {
        for (GridPoint2 treeCoord: treesStartCoordinates) {
            if (treeCoord.equals(position)) {
                return true;
            }
        }

        for (GridPoint2 tankCoord: botTankStartCoordinates) {
            if (tankCoord.equals(position)) {
                return true;
            }
        }

        return false;
    }

    private void initModels(TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        for (GridPoint2 treeCoordinate: treesStartCoordinates) {
            Rectangle treeRectangle = initRectangle(groundLayer, config.treeTexturePath, treeCoordinate);
            treeGraphicModels.add(new TreeGraphicModel(treeRectangle, config.treeTexturePath));
            treeLogicModels.add(new TreeLogicModel(treeCoordinate));
        }

        for (GridPoint2 botTankCoordinate: botTankStartCoordinates) {
            Rectangle botTankRectangle = initRectangle(groundLayer, config.tankTexturePath, botTankCoordinate);
            botTankGraphicModels.add(new TankGraphicModel(botTankRectangle, config.tankTexturePath));
            botTankLogicModels.add(new TankLogicModel(botTankRectangle, tileMovement, config.movementSpeed, botTankCoordinate, obstacles));
        }

        Rectangle playerTankRectangle = initRectangle(groundLayer, config.tankTexturePath, playerTankStartCoordinate);
        playerTankGraphicModel = new TankGraphicModel(playerTankRectangle, config.tankTexturePath);
        playerTankLogicModel = new TankLogicModel(playerTankRectangle, tileMovement, config.movementSpeed, playerTankStartCoordinate, obstacles);

        obstacles.addAll(botTankLogicModels);
        obstacles.addAll(treeLogicModels);
        obstacles.add(playerTankLogicModel);
    }

    private Rectangle initRectangle(TiledMapTileLayer groundLayer, String texturePath, GridPoint2 coordinate) {
        Texture texture = new Texture(texturePath);
        TextureRegion graphics = new TextureRegion(texture);
        Rectangle rectangle = GdxGameUtils.createBoundingRectangle(graphics);
        GdxGameUtils.moveRectangleAtTileCenter(groundLayer, rectangle, coordinate);
        return rectangle;
    }

    public void render(Batch batch) {
        playerTankGraphicModel.render(batch);

        for (IGraphicModel botTankGraphicModel: botTankGraphicModels) {
            botTankGraphicModel.render(batch);
        }

        for (IGraphicModel treeGraphicModel: treeGraphicModels) {
            treeGraphicModel.render(batch);
        }
    }

    public void dispose() {
        playerTankGraphicModel.dispose();

        for (IGraphicModel treeGraphicModel: treeGraphicModels) {
            treeGraphicModel.dispose();
        }

        for (IGraphicModel botTankGraphicModel: botTankGraphicModels) {
            botTankGraphicModel.dispose();
        }
    }
}