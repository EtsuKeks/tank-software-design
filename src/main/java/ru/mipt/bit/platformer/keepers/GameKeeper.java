package ru.mipt.bit.platformer.keepers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.config.ConfigLoader;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.graphicmodels.IGraphicModel;
import ru.mipt.bit.platformer.graphicmodels.TankGraphicModel;
import ru.mipt.bit.platformer.graphicmodels.TreeGraphicModel;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.logicmodels.TreeLogicModel;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GameKeeper {
    public TankLogicModel tankLogicModel;
    public IGraphicModel tankGraphicModel;
    public final ArrayList<TreeLogicModel> treeLogicModels = new ArrayList<>();
    public final ArrayList<IGraphicModel> treeGraphicModels = new ArrayList<>();

    private final GameConfig config;

    private GridPoint2 tankStartPos;
    private final ArrayList<GridPoint2> treesStartCoordinates = new ArrayList<>();

    public GameKeeper(String configFilePath, TiledMapTileLayer groundLayer, TileMovement tileMovement) throws IOException {
        config = ConfigLoader.loadConfig(configFilePath);

        if (config.levelType.equals("file")) {
            createLevelFromFile();
        } else if (config.levelType.equals("random")) {
            createRandomLevel();
        }

        initModels(groundLayer, tileMovement);
    }

    private void createLevelFromFile() throws IOException {
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
                        tankStartPos = new GridPoint2(x, y);
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

    private void createRandomLevel() {
        Random random = new Random();
        int numTrees = random.nextInt(config.obstacleMaxCount - config.obstacleMinCount) + config.obstacleMinCount;

        for (int i = 0; i < numTrees; i++) {
            int x = random.nextInt(config.mapSize.width);
            int y = random.nextInt(config.mapSize.height);
            treesStartCoordinates.add(new GridPoint2(x, y));
        }

        do {
            tankStartPos = new GridPoint2(random.nextInt(config.mapSize.width),
                                          random.nextInt(config.mapSize.height));
        } while (isOccupiedByTree(tankStartPos));
    }

    private boolean isOccupiedByTree(GridPoint2 position) {
        for (GridPoint2 treeCoord: treesStartCoordinates) {
            if (treeCoord.equals(position)) {
                return true;
            }
        }
        return false;
    }

    private void initModels(TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        for (GridPoint2 coordinate: treesStartCoordinates) {
            Texture treeTexture = new Texture(config.treeTexturePath);
            TextureRegion treeGraphics = new TextureRegion(treeTexture);
            Rectangle rectangle = GdxGameUtils.createBoundingRectangle(treeGraphics);
            GdxGameUtils.moveRectangleAtTileCenter(groundLayer, rectangle, new GridPoint2(coordinate.x, coordinate.y));

            treeGraphicModels.add(new TreeGraphicModel(rectangle, config.treeTexturePath));
            treeLogicModels.add(new TreeLogicModel(coordinate.x, coordinate.y));
        }

        Texture tankTexture = new Texture(config.tankTexturePath);
        TextureRegion tankGraphics = new TextureRegion(tankTexture);
        Rectangle tankRectangle = GdxGameUtils.createBoundingRectangle(tankGraphics);
        GdxGameUtils.moveRectangleAtTileCenter(groundLayer, tankRectangle, tankStartPos);

        tankGraphicModel = new TankGraphicModel(tankRectangle, config.tankTexturePath);
        tankLogicModel = new TankLogicModel(tankRectangle, tileMovement, config.movementSpeed, tankStartPos, treeLogicModels);
    }
}