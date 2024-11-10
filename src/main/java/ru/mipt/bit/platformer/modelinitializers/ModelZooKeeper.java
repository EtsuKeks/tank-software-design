package ru.mipt.bit.platformer.modelinitializers;

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
import com.badlogic.gdx.math.Rectangle;
import java.io.IOException;
import java.util.*;

public class ModelZooKeeper {
    public final GameConfig config;

    private TankLogicModel playerTankLogicModel;
    private IGraphicModel playerTankGraphicModel;
    private final Map<TankLogicModel, IGraphicModel> botTankModels = new HashMap<>();
    private final Map<TreeLogicModel, IGraphicModel> treeModels = new HashMap<>();
    private final Map<BulletLogicModel, IGraphicModel> bulletModels = new HashMap<>();
    private final Collection<Obstacle> obstacles = new ArrayList<>();

    public ModelZooKeeper(String configFilePath, TiledMapTileLayer groundLayer, TileMovement tileMovement) throws IOException {
        config = ConfigLoader.loadConfig(configFilePath);

        ModelsInitializer initializer = new ModelsInitializer(this, groundLayer, tileMovement);
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

        for (IGraphicModel bulletGraphicModel: bulletModels.values()) {
            bulletGraphicModel.render(batch);
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

        for (IGraphicModel bulletGraphicModel: bulletModels.values()) {
            bulletGraphicModel.dispose();
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

    public GameConfig getConfig() {
        return config;
    }

    public void notifyBorn(Object o, boolean playable) {
        Obstacle obstacle = (Obstacle) o;
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
                obstacles.add(obstacle);
            }
            case TreeLogicModel treeLogicModel -> {
                TreeGraphicModel treeGraphicModel = new TreeGraphicModel(rectangle, config.treeTexturePath);
                treeModels.put(treeLogicModel, treeGraphicModel);
                obstacles.add(obstacle);
            }
            case BulletLogicModel bulletLogicModel -> {
                BulletGraphicModel bulletGraphicModel = new BulletGraphicModel(rectangle, config.bulletTexturePath, bulletLogicModel.getRotation());
                bulletModels.put(bulletLogicModel, bulletGraphicModel);
            }
            default -> {
                throw new IllegalArgumentException("Class matching exception, got class " + obstacle.getClass());
            }
        }
    }

    public void notifyDead(Object o, boolean playable) {
        Obstacle obstacle = (Obstacle) o;
        switch (obstacle) {
            case TankLogicModel tankLogicModel -> {
                if (playable) {
                    throw new IllegalArgumentException("For now, playerTank can not die");
                } else {
                    botTankModels.get(tankLogicModel).dispose();
                    botTankModels.remove(tankLogicModel);
                }
                obstacles.remove(obstacle);
            }
            case BulletLogicModel bulletLogicModel -> {
                bulletModels.get(bulletLogicModel).dispose();
                bulletModels.remove(bulletLogicModel);
            }
            default -> {
                throw new IllegalArgumentException("Class matching exception, got class " + obstacle.getClass());
            }
        }

    }
}