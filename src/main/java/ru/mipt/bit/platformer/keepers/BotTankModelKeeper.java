package ru.mipt.bit.platformer.keepers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.graphicmodels.HealthBarDecorator;
import ru.mipt.bit.platformer.graphicmodels.TankGraphicModel;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.Collection;

public class BotTankModelKeeper extends ModelCreator implements Observer {
    private final GameConfig gameConfig;
    private final ObstacleModelKeeper obstacleModelKeeper;
    private final GraphicModelKeeper graphicModelKeeper;
    private final TiledMapTileLayer tiledMapTileLayer;
    private final TileMovement tileMovement;
    private final Collection<TankLogicModel> botTankLogicModels = new ArrayList<>();
    private final Collection<TankLogicModel> botTankLogicModelsToRemove = new ArrayList<>();

    @Autowired
    public BotTankModelKeeper(GameConfig gameConfig, ObstacleModelKeeper obstacleModelKeeper,
                                 GraphicModelKeeper graphicModelKeeper, TiledMapTileLayer tiledMapTileLayer, TileMovement tileMovement) {
        this.gameConfig = gameConfig;
        this.obstacleModelKeeper = obstacleModelKeeper;
        this.graphicModelKeeper = graphicModelKeeper;
        this.tiledMapTileLayer = tiledMapTileLayer;
        this.tileMovement = tileMovement;
    }

    @Override
    public void createModel(GridPoint2 coords) {
        Rectangle playerTankRectangle = initRectangle(tiledMapTileLayer, gameConfig.playerTankModelConfig.tankTexturePath, coords);
        botTankLogicModels.add(new TankLogicModel(this, obstacleModelKeeper, playerTankRectangle,
                tileMovement, gameConfig.botTankModelConfig.tankMovementSpeed, coords));
    }

    @Override
    public void notifyBorn(Object o) {
        TankLogicModel tankLogicModel = (TankLogicModel) o;
        TankGraphicModel tankGraphicModelInner = new TankGraphicModel(tankLogicModel.getRectangle(),
                tankLogicModel.getRotation(), gameConfig.playerTankModelConfig.tankTexturePath);
        HealthBarDecorator tankGraphicModel = new HealthBarDecorator(tankGraphicModelInner,
                tankLogicModel.getHealthRatio());
        obstacleModelKeeper.obstacleModels.add(tankLogicModel);
        graphicModelKeeper.graphicModels.put(tankLogicModel, tankGraphicModel);
    }

    @Override
    public void notifyDead(Object o) {
        TankLogicModel botTankLogicModel = (TankLogicModel) o;
        botTankLogicModelsToRemove.add(botTankLogicModel);
    }

    @Override
    public void commitKill() {
        for (TankLogicModel botTankLogicModel: botTankLogicModelsToRemove) {
            botTankLogicModels.remove(botTankLogicModel);
            obstacleModelKeeper.obstacleModels.remove(botTankLogicModel);
            graphicModelKeeper.graphicModels.remove(botTankLogicModel);
        }
    }

    public Collection<TankLogicModel> getBotTankLogicModels() {
        return botTankLogicModels;
    }
}