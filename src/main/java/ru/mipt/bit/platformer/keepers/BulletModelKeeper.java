package ru.mipt.bit.platformer.keepers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import org.springframework.beans.factory.annotation.Autowired;
import ru.mipt.bit.platformer.config.GameConfig;
import ru.mipt.bit.platformer.graphicmodels.BulletGraphicModel;
import ru.mipt.bit.platformer.logicmodels.BulletLogicModel;
import ru.mipt.bit.platformer.logicmodels.TankLogicModel;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.Collection;

public class BulletModelKeeper extends ModelCreator implements Observer {
    private final GameConfig gameConfig;
    private final PlayerTankModelKeeper playerTankModelKeeper;
    private final ObstacleModelKeeper obstacleModelKeeper;
    private final GraphicModelKeeper graphicModelKeeper;
    private final TiledMapTileLayer tiledMapTileLayer;
    private final TileMovement tileMovement;
    private final Collection<BulletLogicModel> bulletLogicModels = new ArrayList<>();
    private final Collection<BulletLogicModel> bulletLogicModelsToRemove = new ArrayList<>();

    @Autowired
    public BulletModelKeeper(GameConfig gameConfig, PlayerTankModelKeeper playerTankModelKeeper,
                             ObstacleModelKeeper obstacleModelKeeper, GraphicModelKeeper graphicModelKeeper,
                             TiledMapTileLayer tiledMapTileLayer, TileMovement tileMovement) {
        this.gameConfig = gameConfig;
        this.graphicModelKeeper = graphicModelKeeper;
        this.obstacleModelKeeper = obstacleModelKeeper;
        this.tiledMapTileLayer = tiledMapTileLayer;
        this.tileMovement = tileMovement;
        this.playerTankModelKeeper = playerTankModelKeeper;
    }

    @Override
    public void createModel(GridPoint2 coords) {
        Rectangle bulletRectangle = initRectangle(tiledMapTileLayer, gameConfig.playerTankModelConfig.bulletTexturePath, coords);
        TankLogicModel playerTankLogicModel = playerTankModelKeeper.getPlayerTankLogicModel();

        bulletLogicModels.add(new BulletLogicModel(this, obstacleModelKeeper, bulletRectangle, tileMovement,
                gameConfig.playerTankModelConfig.bulletMovementSpeed, playerTankLogicModel, gameConfig.playerTankModelConfig.bulletDefaultDamage,
                playerTankLogicModel.getRotation()));
    }

    @Override
    public void notifyBorn(Object o) {
        BulletLogicModel bulletLogicModel = (BulletLogicModel) o;
        BulletGraphicModel bulletGraphicModel = new BulletGraphicModel(bulletLogicModel.getRectangle(),
                gameConfig.playerTankModelConfig.bulletTexturePath, bulletLogicModel.getRotation());

        graphicModelKeeper.graphicModels.put(bulletLogicModel, bulletGraphicModel);
    }

    @Override
    public void notifyDead(Object o) {
        BulletLogicModel bulletLogicModel = (BulletLogicModel) o;
        bulletLogicModelsToRemove.add(bulletLogicModel);
    }

    @Override
    public void commitKill() {
        for (BulletLogicModel bulletLogicModel: bulletLogicModelsToRemove) {
            bulletLogicModels.remove(bulletLogicModel);
            graphicModelKeeper.graphicModels.remove(bulletLogicModel);
        }

        bulletLogicModelsToRemove.clear();
    }

    public Collection<BulletLogicModel> getBulletLogicModels() {
        return bulletLogicModels;
    }
}