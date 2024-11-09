package ru.mipt.bit.platformer.keepers;

import ru.mipt.bit.platformer.util.GameInitializer;
import ru.mipt.bit.platformer.config.ConfigLoader;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.graphicmodels.*;
import ru.mipt.bit.platformer.logicmodels.BulletLogicModel;
import ru.mipt.bit.platformer.logicmodels.Obstacle;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.logicmodels.TreeLogicModel;
import ru.mipt.bit.platformer.util.GdxGameUtils;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.io.IOException;
import java.util.*;

public class ModelZooKeeper {
    private final GameConfig config;
    private TiledMapTileLayer groundLayer;
    private TileMovement tileMovement;

    private TankLogicModel playerTankLogicModel;
    private IGraphicModel playerTankGraphicModel;
    private final Map<TankLogicModel, IGraphicModel> botTankModels = new HashMap<>();
    private final Map<TreeLogicModel, IGraphicModel> treeModels = new HashMap<>();
    private final Map<BulletLogicModel, IGraphicModel> bulletModels = new HashMap<>();
    private final Collection<Obstacle> obstacles = new ArrayList<>();

    public ModelZooKeeper(String configFilePath, TiledMapTileLayer groundLayer, TileMovement tileMovement) throws IOException {
        config = ConfigLoader.loadConfig(configFilePath);
        this.groundLayer = groundLayer;
        this.tileMovement = tileMovement;

        GameInitializer initializer = new GameInitializer(config, this);
        initializer.initialize();
    }

    public void render(Batch batch) {
        playerTankGraphicModel.render(batch);

        for (IGraphicModel botTankGraphicModel: botTankModels.values()) {
            botTankGraphicModel.render(batch);
        }

        for (IGraphicModel treeGraphicModel: treeModels.values()) {
            treeGraphicModel.render(batch);
        }
    }

    public void dispose() {
        playerTankGraphicModel.dispose();

        for (IGraphicModel botTankGraphicModel: botTankModels.values()) {
            botTankGraphicModel.dispose();
        }

        for (IGraphicModel treeGraphicModel: treeModels.values()) {
            treeGraphicModel.dispose();
        }
    }

    public TankLogicModel getPlayerTankLogicModel() {
        return playerTankLogicModel;
    }

    public IGraphicModel getPlayerTankGraphicModel() {
        return playerTankGraphicModel;
    }

    public Map<TankLogicModel, IGraphicModel> getBotTankModels() {
        return botTankModels;
    }

    public Map<TreeLogicModel, IGraphicModel> getTreeModels() {
        return treeModels;
    }

    public Map<BulletLogicModel, IGraphicModel> getBulletModels() {
        return bulletModels;
    }

    public Collection<Obstacle> getObstacles() {
        return obstacles;
    }

    public void initBotTank(GridPoint2 coords) {
        Rectangle botTankRectangle = initRectangle(groundLayer, config.tankTexturePath, coords);
        TankLogicModel botTankLogicModel = new TankLogicModel(botTankRectangle, tileMovement, config.movementSpeed, coords, obstacles);

        if (obstacles.contains(botTankLogicModel)) {
            throw new IllegalArgumentException("There is already a model with coords = " + coords.toString());
        }
        obstacles.add(botTankLogicModel);

        TankGraphicModel botTankGraphicModelInner = new TankGraphicModel(botTankRectangle, config.tankTexturePath);
        HealthBarDecorator botTankGraphicModel = new HealthBarDecorator(botTankGraphicModelInner, playerTankLogicModel);
        botTankModels.put(botTankLogicModel, botTankGraphicModel);
    }

    public void initPlayerTank(GridPoint2 coords) {
        Rectangle playerTankRectangle = initRectangle(groundLayer, config.tankTexturePath, coords);
        playerTankLogicModel = new TankLogicModel(playerTankRectangle, tileMovement, config.movementSpeed, coords, obstacles);

        if (obstacles.contains(playerTankLogicModel)) {
            throw new IllegalArgumentException("There is already a model with coords = " + coords.toString());
        }
        obstacles.add(playerTankLogicModel);

        TankGraphicModel botTankGraphicModelInner = new TankGraphicModel(playerTankRectangle, config.tankTexturePath);
        playerTankGraphicModel = new HealthBarDecorator(botTankGraphicModelInner, playerTankLogicModel);
    }

    public void initTree(GridPoint2 coords) {
        Rectangle treeRectangle = initRectangle(groundLayer, config.treeTexturePath, coords);
        TreeLogicModel treeLogicModel = new TreeLogicModel(coords);

        if (obstacles.contains(treeLogicModel)) {
            throw new IllegalArgumentException("There is already a model with coords = " + coords.toString());
        }
        obstacles.add(treeLogicModel);

        TreeGraphicModel treeGraphicModel = new TreeGraphicModel(treeRectangle, config.treeTexturePath);
        treeModels.put(treeLogicModel, treeGraphicModel);
    }

    public void initBullet(GridPoint2 coords) {

    }

    private Rectangle initRectangle(TiledMapTileLayer groundLayer, String texturePath, GridPoint2 coordinate) {
        Texture texture = new Texture(texturePath);
        TextureRegion graphics = new TextureRegion(texture);
        Rectangle rectangle = GdxGameUtils.createBoundingRectangle(graphics);
        GdxGameUtils.moveRectangleAtTileCenter(groundLayer, rectangle, coordinate);
        return rectangle;
    }
}
