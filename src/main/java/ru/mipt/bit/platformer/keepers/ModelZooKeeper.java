package ru.mipt.bit.platformer.keepers;

import ru.mipt.bit.platformer.util.GameInitializer;
import ru.mipt.bit.platformer.config.ConfigLoader;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.graphicmodels.*;
import ru.mipt.bit.platformer.logicmodels.BulletLogicModel;
import ru.mipt.bit.platformer.logicmodels.Obstacle;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.logicmodels.TreeLogicModel;
import ru.mipt.bit.platformer.util.TileMovement;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import java.io.IOException;
import java.util.*;

public class ModelZooKeeper {
    private final GameConfig config;

    private TankLogicModel playerTankLogicModel;
    private IGraphicModel playerTankGraphicModel;
    private final Map<TankLogicModel, IGraphicModel> botTankModels = new HashMap<>();
    private final Map<TreeLogicModel, IGraphicModel> treeModels = new HashMap<>();
    private final Map<BulletLogicModel, IGraphicModel> bulletModels = new HashMap<>();
    private final Collection<Obstacle> obstacles = new ArrayList<>();

    public ModelZooKeeper(String configFilePath, TiledMapTileLayer groundLayer, TileMovement tileMovement) throws IOException {
        config = ConfigLoader.loadConfig(configFilePath);

        GameInitializer initializer = new GameInitializer(config, this, groundLayer, tileMovement);
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

    public void notifyBorn(Object o, boolean playable) {
        Obstacle obstacle = (Obstacle) o;
        if (obstacles.contains(obstacle)) {
            throw new IllegalArgumentException("There is already an obstacle as being notified from: " + obstacle.toString());
        }
        obstacles.add(obstacle);

        Rectangle rectangle = obstacle.getRectangle();
        switch (obstacle) {
            case TankLogicModel tankLogicModel -> {
                TankGraphicModel tankGraphicModelInner = new TankGraphicModel(rectangle, config.tankTexturePath);
                HealthBarDecorator tankGraphicModel = new HealthBarDecorator(tankGraphicModelInner, tankLogicModel);
                if (playable) {
                    playerTankLogicModel = tankLogicModel;
                    playerTankGraphicModel = tankGraphicModel;
                } else {
                    botTankModels.put(tankLogicModel, tankGraphicModel);
                }
            }
            case TreeLogicModel treeLogicModel -> {
                TreeGraphicModel treeGraphicModel = new TreeGraphicModel(rectangle, config.treeTexturePath);
                treeModels.put(treeLogicModel, treeGraphicModel);
            }
            default -> {
                throw new IllegalArgumentException("Class matching exception, got class " + obstacle.getClass());
            }
        }
    }

    public void notifyDead(Object o) {

    }

    public void initBullet(GridPoint2 coords) {

    }
}
